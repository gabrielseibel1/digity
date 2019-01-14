package com.digity

import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import org.tensorflow.TensorFlow

fun initTensorFlow() {
    Graph().use { graph ->
        val value = "Hello from ${TensorFlow.version()}"

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