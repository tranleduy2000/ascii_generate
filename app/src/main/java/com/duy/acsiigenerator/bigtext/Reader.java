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

package com.duy.acsiigenerator.bigtext;

import com.duy.acsiigenerator.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by Duy on 07-Jul-17.
 */

public class Reader {
    private boolean loaded = false;
    private ArrayList<HashMap<Character, String>> fonts;

    public Reader() {
        fonts = new ArrayList<>();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void loadAndClose(InputStream[] inputStream) {
        for (InputStream stream : inputStream) {
            try {
                Matcher matcher = FileUtil.PATTERN_SLIP.matcher(FileUtil.streamToString(stream));
                HashMap<Character, String> font = new HashMap<>();
                for (int i = 'A'; i < 'Z' && matcher.find(); i++) {
                    font.put((char) i, matcher.group());
                }
                fonts.add(font);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        loaded = true;
    }

    public int getSize() {
        return fonts.size();
    }

    public String convert(String text, int position) {
        HashMap<Character, String> hashMap = fonts.get(position);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String s = hashMap.get(text.charAt(i));
            result.append(s == null ? text.charAt(i) : s);
        }
        return result.toString();
    }

}
