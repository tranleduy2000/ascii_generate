adb uninstall com.duy.asciigenerator.pro
adb install -r release/app-pro-release.apk
adb shell am start -n " com.duy.asciigenerator.pro/com.duy.ascii.art.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
exit