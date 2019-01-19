import com.digity.ImagePreprocessor
import kotlin.test.Test

class ImagePreprocessorTest {

    @Test
    fun testResizeTo28x28() {
        val ipp = ImagePreprocessor()
        ipp.resizeTo28x28("image/lena1.png")
    }
}