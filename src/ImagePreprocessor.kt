package com.digity

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.core.Mat

class ImagePreprocessor {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        println("Welcome to OpenCV ${Core.VERSION}")
    }

    fun getFloatArray(imagePath: String): Array<Float> {
        resizeInvertAndGray(imagePath)

        // read image
        val image = Imgcodecs.imread(imagePath)

        // add each pixel to list
        val floats = mutableListOf<Float>()
        for (row in 0 until image.height()) {
            for (col in 0 until image.width()) {
                floats.add(image.get(row, col)[0].toFloat())
            }
        }

        return floats.toTypedArray()
    }

    private fun resizeInvertAndGray(imagePath: String) {
        // read image
        val wrongSizedImage = Imgcodecs.imread(imagePath)

        // resize it
        val image = Mat()
        Imgproc.resize(wrongSizedImage, image, Size(28.0, 28.0))

        // to grayscale if needed
        if (image.channels() == 3) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY)
        }

        // take the negative
        image.convertTo(image, -1, -1.0, 255.0)

        // save it altered
        Imgcodecs.imwrite(imagePath, image)
    }
}