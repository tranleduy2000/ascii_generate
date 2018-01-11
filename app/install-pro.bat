adb uninstall com.duy.asciigenerator.pro
adb install -r C:\github\ascii_generate\app\build\outputs\apk\pro\release\app-pro-release.apk
adb shell am start -n " com.duy.asciigenerator.pro/com.duy.ascii.art.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
exit