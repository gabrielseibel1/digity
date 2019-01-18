import com.digity.Mnist
import com.digity.indexOfMax
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

    @Test
    fun testMnistAccuracy() {
        withTestApplication {
            println("Hello from TensorFlow ${TensorFlow.version()}")

            // obtain test data
            val mnist = Mnist("mnist/t10k-images.idx3-ubyte", "mnist/t10k-labels.idx1-ubyte")
            assert(mnist.valid)

            // obtain saved model
            val model = SavedModelBundle.load("model", "serve")
            val graph = model.graph()
            val session = model.session()

            // count correct predictions over dataset
            var correctPredictions = 0
            graph.use {
                session.use {
                    mnist.images.forEachIndexed { index, image ->

                        // feed image
                        val result = session.runner()
                            .feed("input_tensor", Tensor.create(arrayOf(image)))
                            .fetch("output_layer/Softmax")
                            .run()[0]

                        // get result
                        val probabilities = arrayOf(FloatArray(10))
                        result.copyTo(probabilities)
                        val prediction = probabilities[0].toList().indexOfMax()

                        // compare with expected label
                        val expectedLabel = mnist.labels[index]
                        if (prediction == expectedLabel) ++correctPredictions

                        println("Expected $expectedLabel and got $prediction (${probabilities[0].toList()})")
                    }
                }
            }

            val accuracy = correctPredictions/mnist.images.size
            println("Accuracy for MNIST test set = $accuracy")
            assert(accuracy > 97F)
        }
    }


}
