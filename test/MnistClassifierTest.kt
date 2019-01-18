
import com.digity.Mnist
import com.digity.MnistClassifier
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.tensorflow.SavedModelBundle
import org.tensorflow.Tensor
import org.tensorflow.TensorFlow
import kotlin.test.Test

class MnistClassifierTest {

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
                    val input = Array(1) { Array(28) { Array(28) { 0F } } }

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
            val classifier = MnistClassifier()

            // count correct predictions over dataset
            var correctPredictions = 0
            classifier.use {
                mnist.images.forEachIndexed { index, image ->

                    val prediction = classifier.predict(image)

                    // compare with expected label
                    val expectedLabel = mnist.labels[index]
                    if (prediction == expectedLabel) ++correctPredictions

                    println(image.toList())
                    println("Expected $expectedLabel and got $prediction")
                }
            }

            val accuracy = correctPredictions.toFloat()/mnist.images.size
            println("Accuracy for MNIST test set = $accuracy ($correctPredictions/${mnist.images.size})")
            assert(accuracy > 0.97F)
        }
    }

}
