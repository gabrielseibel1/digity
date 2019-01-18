package com.digity

import java.io.File
import java.nio.ByteBuffer

class Mnist(private val imagesPath: String, private val labelsPath: String) {

    val images: List<Array<Float>>
    val labels: List<Int>
    val valid: Boolean

    init {
        if (validImagesHeader() and validLabelsHeader()) {

            images = readImages()
            labels = readLabels()

        } else {

            images = emptyList()
            labels = emptyList()

        }

        valid = images.size == EXPECTED_NUM_ITEMS && labels.size == EXPECTED_NUM_ITEMS
    }

    private fun validImagesHeader(): Boolean {
        val imagesFile = File(imagesPath)

        val imagesBytes = imagesFile.readBytes()

        val checkNumber = imagesBytes.copyOfRange(0, 4).asInt()
        val numImages = imagesBytes.copyOfRange(4, 8).asInt()
        val numRows = imagesBytes.copyOfRange(8, 12).asInt()
        val numCols = imagesBytes.copyOfRange(12, 16).asInt()

        return checkNumber == IMAGES_VALIDATION_NUMBER &&
                numImages == EXPECTED_NUM_ITEMS &&
                numRows == EXPECTED_IMAGE_SIZE &&
                numCols == EXPECTED_IMAGE_SIZE
    }

    private fun validLabelsHeader(): Boolean {
        val labelsFile = File(labelsPath)

        val labelsBytes = labelsFile.readBytes()

        val checkNumber = labelsBytes.copyOfRange(0, 4).asInt()
        val numLabels = labelsBytes.copyOfRange(4, 8).asInt()

        return checkNumber == IMAGES_VALIDATION_NUMBER &&
                numLabels == EXPECTED_NUM_ITEMS
    }

    private fun readImages(): List<Array<Float>> {
        val allImages = mutableListOf<Array<Float>>()

        val imagesBytes = File(imagesPath).readBytes()

        // skip header
        var byteOffset = IMAGES_HEADER_BYTES

        // build a pixel array for each item
        for (i in 0 until EXPECTED_NUM_ITEMS) {
            val pixels = mutableListOf<Float>()

            for (p in 0 until EXPECTED_NUM_ITEMS * EXPECTED_NUM_ITEMS) {
                val pixel = imagesBytes.copyOfRange(byteOffset, ++byteOffset).asFloat()
                pixels.add(pixel)
            }

            allImages.add(pixels.toTypedArray())
        }

        return allImages.toList()
    }

    private fun readLabels(): List<Int> {
        val allLabels = mutableListOf<Int>()
        val labelsBytes = File(labelsPath).readBytes()

        // skip header
        var byteOffset = LABELS_HEADER_BYTES

        // add a label to the list for each item
        for (i in 0 until EXPECTED_NUM_ITEMS) {

            val label = labelsBytes.copyOfRange(byteOffset, ++byteOffset).asInt()
            allLabels.add(label)
        }

        return allLabels.toList()
    }

    private fun ByteArray.asInt(): Int {
        val buffer = ByteBuffer.wrap(this)
        return buffer.int
    }

    private fun ByteArray.asFloat(): Float {
        val buffer = ByteBuffer.wrap(this)
        return buffer.float
    }

    private companion object {
        private const val IMAGES_VALIDATION_NUMBER = 2051
        private const val LABELS_VALIDATION_NUMBER = 2049
        private const val EXPECTED_NUM_ITEMS = 10000
        private const val EXPECTED_IMAGE_SIZE = 28
        private const val IMAGES_HEADER_BYTES = 16 //four 32bit Ints
        private const val LABELS_HEADER_BYTES = 8 //two 32bit Ints
    }
}