# ECDHTest
Test Project for ECDH and CryptoPP C++ JNI with Android Studio

## Building This Project
* **CryptoPP Compile:**
You can find the steps to compile CryptoPP for Android [here](https://www.cryptopp.com/wiki/Android_(Command_Line))
**OR** you can download my pre-compiled version from [here](http://www.mediafire.com/download/npt73f9369vuy1n/cryptopp.zip) (android-18 for arm, armeabi-v7a, x86).

* **Make sure** that if you download my pre-compiled versions, that you put the cryptopp folder inside your /usr/local/ directory, since this is where the Android.mk file will look for them.

* I found [this](https://www.youtube.com/watch?v=RmPuwdxR1qs) Youtube tutorial very helpful in figuring how to setup the JNI structure as well as adding some helpful javah and ndk-build tools to Android Studio.

* In the Android.mk file, make sure you change the **STLPORT_INCL** and **STLPORT_LIB** lines to point specifically to **your** ndk folder. For example, mine is located on my computer at '/Users/ThomasColligan/Library/Android/sdk/ndk-bundle/'

## Running This Project

If you just build and run the project as is, you should have no issues, however if you modify the ecdhlibrary.cpp file you will need to run the ndk-build command yourself to re-compile the .so files.

## What does it do?
This project has some Java wrapper methods that calls the CryptoPP C++ Librarys ECDH code to generate public and private key pairs, as well as extract the shared secret from said public private key pairs. It uses Android studio and the NDK (non-experimental) to accomplish this.
