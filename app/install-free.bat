copy build\outputs\apk\free\release\app-free-release.apk release/app-free-release.apk
copy build\outputs\apk\pro\release\app-pro-release.apk release/app-pro-release.apk

adb uninstall com.duy.asciiart
adb install -r release/app-free-release.apk
adb shell am start -n "com.duy.asciiart/com.duy.ascii.art.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
