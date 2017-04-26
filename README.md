
[![Release](https://jitpack.io/v/roadhouse-dev/RxDbflow.svg)](https://jitpack.io/#roadhouse-dev/RxDbflow)

# RxDBFlow
RxJava bindings for DBFlow models, and query language

# Download

Add the JitPack repository to your root build file

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```


For RxJava1 add the dependencies:
```groovy
dependencies {
    apt 'com.github.Raizlabs.DBFlow:dbflow-processor:4.0.0-beta7'
    compile 'com.github.Raizlabs.DBFlow:dbflow-core:4.0.0-beta7'
    compile 'com.github.Raizlabs.DBFlow:dbflow:4.0.0-beta7'
    compile 'com.github.roadhouse-dev.RxDbflow:rxdbflow-rx1:<Version>'
}
```

or for RxJava2 add the dependencies:

```groovy
dependencies {
    apt 'com.github.Raizlabs.DBFlow:dbflow-processor:3.1.1'
    compile 'com.github.Raizlabs.DBFlow:dbflow-core:3.1.1'
    compile 'com.github.Raizlabs.DBFlow:dbflow:3.1.1'
    compile 'com.github.roadhouse-dev.RxDbflow:rxdbflow-rx2:<Version>'
}
```

At the moment only DBFlow version 4.0.0-beta7 is supported

# Build
```bash
$ git clone https://github.com/roadhouse-dev/RxDbflow.git
$ ./gradlew build
```

# DBFlow documentation
For help with DBFlow please take a look at [DBFlow GitHub](https://github.com/Raizlabs/DBFlow)

# Scheduler
RxDBFlow now comes with it's own scheduler (DBFlowSchedulers.background()), this is a single threaded scheduler that will ensure
all database operations happen from the same background thread. Using a multi-threaded scheduler like Schedulers.io() can cause deadlocks.


# Sample usage
There are slight differences between rx1 and rx2

# Rx1 Sample usage
[View here](README-RX1.md)

# Rx2 Sample usage
[View here](README-RX2.md)


# Bugs and Feedback
For RxDBFlow bugs, questions and discussions please use the [GitHub Issues](https://github.com/roadhouse-dev/RxDbflow/issues).

For DBFlow specific bugs, questions and discussions please use the [GitHub Issues](https://github.com/Raizlabs/DBFlow/issues).

# Pull Requests
All pull requests are welcome, however to make the whole process smoother please use the following guides

* All pull requests should be against the ```develop``` branch
* Code formatting should match the default Android Studio format
* Limit code changes to the scope of what you're implementing
* Provide standard JavaDoc for any public accessible members and classes
