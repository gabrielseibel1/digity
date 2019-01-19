import com.digity.ImagePreprocessor
import kotlin.test.Test

class ImagePreprocessorTest {

    @Test
    fun testResizeTo28x28() {
        val ipp = ImagePreprocessor()
        val floats = ipp.getFloatArray("image/lena1.png")
        assert(floats.size == 784)
    }
}