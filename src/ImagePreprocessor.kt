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

    fun resizeTo28x28(imagePath: String) {
        // read image
        val image = Imgcodecs.imread(imagePath)

        // resize it
        val resized = Mat()
        Imgproc.resize(image, resized, Size(28.0, 28.0))


        // save it resized
        Imgcodecs.imwrite(imagePath, resized)
    }
}