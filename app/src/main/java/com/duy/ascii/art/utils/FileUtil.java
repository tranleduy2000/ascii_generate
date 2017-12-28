/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ascii.art.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by Duy on 03-Jul-17.
 */

public class FileUtil {
    public static final Pattern PATTERN_SLIP = Pattern.compile("(\")(.*?)(\")", Pattern.DOTALL);
    private static final String TAG = "FileUtil";
    private static final String IMAGE_FOLDER_NAME = "Image";

    public static String streamToString(@NonNull InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    public static File getImageDirectory(Context context) {
        if (hasSdCard(context)) {
            File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FOLDER_NAME);
            if (!file.exists()) file.mkdir();
            return file;
        }
        File file = new File(context.getFilesDir(), IMAGE_FOLDER_NAME);
        if (!file.exists()) file.mkdir();
        return file;
    }

    private static boolean hasSdCard(Context context) {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
