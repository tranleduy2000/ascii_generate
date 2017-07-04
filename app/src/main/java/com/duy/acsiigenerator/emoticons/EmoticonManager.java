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
