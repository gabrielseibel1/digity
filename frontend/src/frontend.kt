import org.w3c.dom.*
import org.w3c.dom.url.URL
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
                open("POST", "http://localhost:8080")
                onreadystatechange = {

                    // if call succeeded
                    if (request.readyState == XMLHttpRequest.DONE && request.status.toInt() == 200) {

                        updateUI("", 0)
                        println(request.response)
                    }

                }
                send(formData)
            }
        }

    })
}

fun updateUI(imageURL: String, prediction: Int) {
    val image = document.getElementById("digitImage") as HTMLImageElement
    image.src = "https://www.gettyimages.com/gi-resources/images/CreativeLandingPage/HP_Sept_24_2018/CR3_GettyImages-159018836.jpg"
}
