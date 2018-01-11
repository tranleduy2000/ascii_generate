copy build\outputs\apk\free\release\app-free-release.apk release/app-free-release.apk
adb uninstall com.duy.asciiart
adb install -r release/app-free-release.apk
adb shell am start -n "com.duy.asciiart/com.duy.ascii.art.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
