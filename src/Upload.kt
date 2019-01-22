package com.digity

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.*
import kotlinx.html.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream


@KtorExperimentalLocationsAPI
fun Route.upload(uploadDir: File) {

    /**
     * Route to serve HTML to upload file
     */
    get<Upload> {
        call.respondHtml {
            uploadPageHTML(this@get)
        }
    }

    /**
     * Route to process uploaded file
     */
    post<Upload> {
        val multipart = call.receiveMultipart()
        var title = String()
        var imageFile: File? = null

        // multipart processing
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "title") title = part.value
                }

                //copy part content to server-side file
                is PartData.FileItem -> {
                    val extension = File(part.originalFileName).extension
                    val file = File(
                        uploadDir,
                        "upload-${System.currentTimeMillis()}-${title.hashCode()}.$extension"
                    )

                    //open/close input and output streams to copy one to the other
                    part.streamProvider().use { inputStream ->
                        file.outputStream().buffered().use { outputStream ->

                            inputStream.copyToSuspend(outputStream)
                        }
                    }

                    imageFile = file
                }

                else -> {
                }
            }

            part.dispose()
        }

        if (imageFile == null) {

            call.respondText { "Unable to process upload :(" }

        } else {

            // image pre-processing
            val imagePath = uploadDir.path + "/" + imageFile!!.name
            val floatArray = ImagePreprocessor().getFloatArray(imagePath)
            floatArray.displayImage()

            // prediction
            val classifier = MnistClassifier()
            val prediction = classifier.predict(floatArray)

            call.respond(
                mapOf(
                    "image" to "http://localhost:8080/static/${imageFile!!.name}",
                    "prediction" to prediction
                )
            )

            // delete image when server stops running
            imageFile!!.deleteOnExit()
        }

    }
}

private fun removePastUploads(uploadDir: File) {
    uploadDir.walk().forEach { file ->
        if (file.name.startsWith("upload-")) file.delete()
    }
}

private fun HTML.predictionPageHTML(imageName: String, prediction: Int) {
    head {
        title {
            +"Prediction"
        }
    }
    body {
        img { src = "http://localhost:8080/static/$imageName" }
        h3 { +"Is this $prediction?" }
        form(
            action = "http://localhost:8080"
        ) {
            submitInput(classes = "pure-button pure-button-primary") { value = "Try again" }
        }
    }
}

@KtorExperimentalLocationsAPI
private fun HTML.uploadPageHTML(pipelineContext: PipelineContext<Unit, ApplicationCall>) {
    head {
        title { +"Upload Digit" }
    }
    body {
        h1 { +"Upload Digit" }
        p {
            +"Please choose an image that contains the drawing of a single digit."
        }

        form(
            action = pipelineContext.call.url(Upload()),
            encType = FormEncType.multipartFormData,
            method = FormMethod.post,
            classes = "pure-form-stacked"
        ) {
            acceptCharset = "utf-8"

            fileInput { name = "file"; accept = "image/jpg, image/png" }
            submitInput(classes = "pure-button pure-button-primary") { value = "Upload" }
        }
    }
}

/**
 * Copies [this] [InputStream] into [out] [OutputStream] in a suspending coroutine
 *
 * [bufferSize] and [yieldSize] control how and when the suspending is performed.
 * [dispatcher] allows to specify where the coroutine will be executed (for example a specific thread pool).
 */
suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}