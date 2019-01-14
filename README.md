# Digity

Digity is an application built in Kotlin, to run on a web browser, using Ktor.

It accepts a user-uploaded image and classifies it as a digit [0-9] using Tensorflow.

## Environment Setup

    # install bazel
    sudo apt-get install openjdk-8-jdk
    echo "deb [arch=amd64] http://storage.googleapis.com/bazel-apt stable jdk1.8" | sudo tee /etc/apt/sources.list.d/bazel.list
    curl https://bazel.build/bazel-release.pub.gpg | sudo apt-key add -
    sudo apt-get update && sudo apt-get install bazel
    sudo apt-get install --only-upgrade bazel
    
    # tensorflow environment
    sudo apt-get install python swig python-numpy
    sudo pip install six numpy wheel
    
    # install tensorflow
    git clone https://github.com/tensorflow/tensorflow.git
    cd tensorflow
    bazel build --config=opt //tensorflow/tools/pip_package:build_pip_package
    sudo pip install /tmp/tensorflow_pkg/tensorflow-1.0.1-py2-none-any.whl