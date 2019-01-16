import tensorflow as tf

export_dir = '../model'

mnist = tf.keras.datasets.mnist

# noinspection PyUnresolvedReferences
# x is images and y is labels
(x_train, y_train), (x_test, y_test) = mnist.load_data()
x_train, x_test = x_train / 255.0, x_test / 255.0

# noinspection PyUnresolvedReferences
model = tf.keras.models.Sequential([
    tf.keras.layers.Flatten(),  # turns 28x28 matrix into size 784 array
    tf.keras.layers.Dense(512, activation=tf.nn.relu),  # 512 neurons
    tf.keras.layers.Dropout(0.2),
    tf.keras.layers.Dense(10, activation=tf.nn.softmax)  # probability scores with sum 1
])
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',  # function to minimize
              metrics=['accuracy'])  # metrics monitored in training and testing

# training
model.fit(x_train, y_train, epochs=5)
# testing
model.evaluate(x_test, y_test)

# save model in binary form for inference (serving) in JAVA
builder = tf.saved_model.builder.SavedModelBuilder(export_dir)
builder.add_meta_graph_and_variables(tf.keras.backend.get_session(), [tf.saved_model.tag_constants.SERVING])
builder.save(False)
