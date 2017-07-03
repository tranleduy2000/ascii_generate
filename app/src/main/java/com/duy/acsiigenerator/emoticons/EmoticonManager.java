package com.duy.acsiigenerator.emoticons;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duy on 03-Jul-17.
 */

public class EmoticonManager {
    public static final Pattern PATTERN = Pattern.compile("(\")(.*?)(\")", Pattern.DOTALL);
    private static final String TAG = "EmoticonManager";

    public static ArrayList<String> readFaces(InputStream inputStream) throws IOException {
        String string = FileUtil.streamToString(inputStream);
        Matcher matcher = PATTERN.matcher(string);
        ArrayList<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group(2));
            Log.d(TAG, "readFaces: " + matcher.group());
        }
        return result;
    }
}
