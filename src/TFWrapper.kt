package com.digity

import org.tensorflow.*

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

fun List<Float>.indexOfMax(): Int {
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