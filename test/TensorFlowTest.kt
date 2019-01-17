import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.tensorflow.SavedModelBundle
import org.tensorflow.Tensor
import org.tensorflow.TensorFlow
import kotlin.test.Test

class TensorFlowTest {

    @KtorExperimentalAPI
    @Test
    fun testLoadModel() {
        withTestApplication {
            println("Hello from TensorFlow ${TensorFlow.version()}")

            // obtain saved model
            /*val config = environment.config.config("digity")
            val modelDirPath = config.property("dirs.model").getString()*/
            val model = SavedModelBundle.load("model", "serve")

            // obtain its graph and session
            val graph = model.graph()
            val session = model.session()


            graph.use {
                session.use {
                    val input = Array(1) { Array(784) { 0F } }

                    val result = session.runner()
                        .feed("input_tensor", Tensor.create(input))
                        .fetch("output_layer/Softmax")
                        .run()[0]

                    val probabilities = arrayOf(FloatArray(10))
                    result.copyTo(probabilities)
                    println(result)
                    println(probabilities[0].toList())
                }
            }
        }
    }
}
