#!/bin/bash -x

dir=$(pwd)

ant clean
ant

cp dist/com.activate.gcm-android-0.8.zip ~/Desktop/
cd ~/Desktop
unzip com.activate.gcm-android-0.8.zip

rm -fr /Users/sngmr/Projects/MyApp/sources/titanium/demo/modules/android/com.activate.gcm/0.8
mv modules/android/com.activate.gcm/0.8 /Users/sngmr/Projects/MyApp/sources/titanium/demo/modules/android/com.activate.gcm/

rm -fr com.activate.gcm-android-0.8.zip
rm -fr modules/

cd "${dir}"

