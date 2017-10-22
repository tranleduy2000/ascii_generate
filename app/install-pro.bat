cd app
adb uninstall com.duy.asciiart
adb uninstall com.duy.asciigenerator.pro
adb install -r app-pro-release.apk
adb shell am start -n " com.duy.asciigenerator.pro/com.duy.ascii.sharedcode.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
exit