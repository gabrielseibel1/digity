package com.digity

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.coroutines.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream


@KtorExperimentalLocationsAPI
fun Route.upload(uploadDir: File) {

    /**
     * Route to process uploaded file
     */
    post<API.Upload> {
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

            // mark to delete image when server stops running
            imageFile!!.deleteOnExit()

            // respond with prediction object
            call.respond(
                PredictionResult(prediction, ApiEndpoint.ROOT + "/static/${imageFile!!.name}")
            )
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