import com.digity.Mnist
import kotlin.test.Test

class MnistTest {

    @Test
    fun testMnistInit() {
        val mnist = Mnist("mnist/t10k-images.idx3-ubyte", "mnist/t10k-labels.idx1-ubyte")
        if (mnist.valid) {
            print("Images:\n${mnist.images}")
            print("\n\n\n")
            print("Labels:\n${mnist.labels}")
        } else {
            println("Mnist reading failed with " +
                    "#images=${mnist.images.size} and " +
                    "#labels=${mnist.labels.size}.")
        }

        assert(mnist.valid)
    }
}