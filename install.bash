#!/bin/bash -x

dir=$(pwd)

ant clean
ant

cp dist/com.activate.gcm-android-0.7.zip ~/Desktop/
cd ~/Desktop
unzip com.activate.gcm-android-0.7.zip

rm -fr /Users/sngmr/Projects/MyApp/sources/titanium/handl/modules/android/com.activate.gcm/0.7
mv modules/android/com.activate.gcm/0.7 /Users/sngmr/Projects/MyApp/sources/titanium/handl/modules/android/com.activate.gcm/

rm -fr com.activate.gcm-android-0.7.zip
rm -fr modules/

cd "${dir}"

