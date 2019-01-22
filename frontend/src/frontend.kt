import org.w3c.dom.*
import org.w3c.files.get
import org.w3c.xhr.FormData
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document

fun main(args: Array<String>) {
    val inputElement = document.getElementById("myFileField") as HTMLInputElement
    val button = document.getElementById("myButton") as HTMLButtonElement

    // attach function to button
    button.addEventListener("click", {

        // if user has chosen file
        inputElement.files?.get(0)?.let { file ->

            val formData = FormData()
            formData.append("file", file)

            val request = XMLHttpRequest()
            with(request) {
                open("POST", ApiEndpoint.UPLOAD.path())
                onreadystatechange = {

                    // if call succeeded, update UI accordingly
                    if (request.readyState == XMLHttpRequest.DONE && request.status.toInt() == 200) {

                        val predictionResult = JSON.parse<PredictionResult>(response.toString())
                        predictionResult.draw()
                    }

                }
                send(formData)
            }
        }

    })
}

fun PredictionResult.draw() {
    val image = document.getElementById("digitImage") as HTMLImageElement
    val text = document.getElementById("predictionText") as HTMLParagraphElement

    image.src = imageURL
    text.innerText = "Is this $prediction ?"
}
