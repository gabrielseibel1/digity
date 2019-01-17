package com.digity

import java.io.File
import java.nio.ByteBuffer

class Mnist(imagesPath: String, labelsPath: String)
    : Iterable<Array<Float>> {
    override fun iterator(): Iterator<Array<Float>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    init {
        val imagesFile = File(imagesPath)
        val labelsFile = File(labelsPath)

        val imagesBytes = imagesFile.readBytes()
        val labelsBytes = labelsFile.readBytes()

        val magicNumberBytes = imagesBytes.copyOfRange(0, 4)
        val numImages = imagesBytes.copyOfRange(4, 8)
        val numRowsBytes = imagesBytes.copyOfRange(8, 12)
        val numColsBytes = imagesBytes.copyOfRange(12, 16)

        println("MN=${magicNumberBytes.asInt()}, " +
                "#images=${numImages.asInt()}, " +
                "#rows=${numRowsBytes.asInt()}, " +
                "#cols=${numColsBytes.asInt()}")
    }

    private fun ByteArray.asInt(): Int {
        val buffer = ByteBuffer.wrap(this)
        return buffer.int
    }
}