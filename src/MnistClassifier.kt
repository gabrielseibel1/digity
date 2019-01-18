package com.digity

import org.tensorflow.*
import java.io.Closeable
import java.nio.FloatBuffer

class MnistClassifier : Closeable {

    val model: SavedModelBundle = SavedModelBundle.load("model", "serve")

    val session = model.session()

    fun predict(image: Array<Float>): Int {
        // feed image
        val fb = FloatBuffer.wrap(image.toFloatArray())
        val result = session.runner()
            .feed("input_tensor", Tensor.create(longArrayOf(1, 28, 28), fb))
            .fetch("output_layer/Softmax")
            .run()[0]

        // get result
        val probabilities = arrayOf(FloatArray(10))
        result.copyTo(probabilities)
        return probabilities[0].toList().indexOfMax()
    }

    override fun close() {
        session.close()
    }

    fun initTensorFlow() {
        Graph().use { graph ->
            val value = "Hello from TensorFlow ${TensorFlow.version()}"

            // Construct the computation graph with a single operation, a constant
            // named "MyConst" with a value "value".
            Tensor.create(value.toByteArray()).use { tensor ->
                graph.opBuilder("Const", "MyConst")
                    .setAttr("dtype", tensor.dataType())
                    .setAttr("value", tensor).build()
            }

            // Execute the "MyConst" operation in a Session.
            Session(graph).use { session ->

                // Generally, there may be multiple output tensors,
                // all of them must be closed to prevent resource leaks.
                session.runner().fetch("MyConst").run()[0].use { output ->
                    println(String(output.bytesValue(), Charsets.UTF_8))
                }
            }
        }
    }

    private fun List<Float>.indexOfMax(): Int {
        var max = 0F
        var maxIndex = 0

        this.forEachIndexed { index, float ->
            if (float > max) {
                max = float
                maxIndex = index
            }
        }

        return maxIndex
    }
}
