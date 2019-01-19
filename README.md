# Digity

Digity is an application built in Kotlin, to run on a web browser, using Ktor.

It accepts a user-uploaded image and classifies it as a digit [0-9] using Tensorflow.


## TensorFlow Setup
### Python Environment Setup (Training)
https://www.tensorflow.org/install/pip

### Java Environment Setup (Predicting)

Build Tensorflow Java API:

    # install bazel
    sudo apt-get install openjdk-8-jdk
    echo "deb [arch=amd64] http://storage.googleapis.com/bazel-apt stable jdk1.8" | sudo tee /etc/apt/sources.list.d/bazel.list
    curl https://bazel.build/bazel-release.pub.gpg | sudo apt-key add -
    sudo apt-get update && sudo apt-get install bazel
    sudo apt-get install --only-upgrade bazel
    
    # install tensorflow
    sudo apt-get install python swig python-numpy
    git clone https://github.com/tensorflow/tensorflow.git
    cd tensorflow
    ./configure
    bazel build --config opt \
      //tensorflow/java:tensorflow \
      //tensorflow/java:libtensorflow_jni

Add generated libtensorflow.jar and libtensorflow_jni.so to idea project as library.

## OpenCV Setup
https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html

Add generated opencv_3xx.jar and libopencv_java3xx.so to idea project as library.
