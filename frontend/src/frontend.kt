import org.w3c.dom.*
import org.w3c.files.get
import org.w3c.xhr.FormData
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document

fun main(args: Array<String>) {
    val inputElement = document.getElementById("filePicker") as HTMLInputElement
    val button = document.getElementById("predictButton") as HTMLButtonElement

    // attach function to button
    button.addEventListener("click", {

        // if user has chosen file
        inputElement.files?.get(0)?.let { file ->

            // get form data to append to request
            val formData = FormData()
            formData.append("file", file)

            // build and send POST request
            val request = XMLHttpRequest()
            with(request) {
                open("POST", ApiEndpoint.UPLOAD.path())
                onreadystatechange = {

                    // if call succeeded, update UI accordingly
                    if (request.readyState == XMLHttpRequest.DONE && request.status.toInt() == 200) {

                        val result = JSON.parse<ApiResult<PredictionResult>>(response.toString())
                        result.draw()
                    }

                }
                send(formData)
            }
        }

    })
}

fun ApiResult<PredictionResult>.draw() {
    val image = document.getElementById("digitImage") as HTMLImageElement
    val text = document.getElementById("predictionText") as HTMLParagraphElement

    if (success) {

        image.src = result.imageURL
        text.innerText = "Is this ${result.prediction} ?"

    } else {

        image.src = "http://localhost:8080/static/question_mark.png"
        text.innerText = message

    }
}
