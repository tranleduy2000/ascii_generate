package com.duy.acsiigenerator.emoticons;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Duy on 03-Jul-17.
 */

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static String streamToString(@NonNull InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
