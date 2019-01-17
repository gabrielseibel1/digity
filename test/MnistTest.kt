import com.digity.Mnist
import kotlin.test.Test

class MnistTest {

    @Test
    fun testMnistInit() {
        val mnist = Mnist("mnist/t10k-images.idx3-ubyte", "mnist/t10k-labels.idx1-ubyte")
    }
}