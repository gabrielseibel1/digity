import com.digity.ImagePreprocessor
import kotlin.test.Test

class ImagePreprocessorTest {

    @Test
    fun testResizeTo28x28() {
        val ipp = ImagePreprocessor()
        ipp.getFloatArray("../lena1.png")
    }
}