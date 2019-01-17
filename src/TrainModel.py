import shutil
import os
import tensorflow as tf
from tensorflow.keras.layers import InputLayer
from tensorflow.keras.layers import Flatten
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import Dropout

# define model dir and delete it if it already exists
export_dir = '../model'
if os.path.isdir(export_dir):
    shutil.rmtree(export_dir)

# retrieve dataset
mnist = tf.keras.datasets.mnist

# noinspection PyUnresolvedReferences
# x is images and y is labels
(x_train, y_train), (x_test, y_test) = mnist.load_data()
x_train, x_test = x_train / 255.0, x_test / 255.0

# define the model
model = tf.keras.models.Sequential([
    InputLayer(input_shape=(28, 28), name="input_tensor"),  # define input tensor for JAVA ease of use
    Flatten(),  # turns (28x28) matrix into (size 784) array
    Dense(512, activation=tf.nn.relu, name="dense1"),  # 512 neurons
    Dropout(0.2),  # randomly zero some values to avoid overfitting
    Dense(10, activation=tf.nn.softmax, name="output_layer")  # 10 neurons - probability scores with sum 1
])
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',  # function to minimize
              metrics=['accuracy'])  # metrics monitored in training and testing

print(model.outputs)
print([node.op.name for node in model.outputs])

# training
model.fit(x_train, y_train, epochs=5)
# testing
model.evaluate(x_test, y_test)

model.summary()

# save model in binary form for inference (serving) in JAVA
builder = tf.saved_model.builder.SavedModelBuilder(export_dir)
builder.add_meta_graph_and_variables(tf.keras.backend.get_session(), [tf.saved_model.tag_constants.SERVING])
builder.save(True)
