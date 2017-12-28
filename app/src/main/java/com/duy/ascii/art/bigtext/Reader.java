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

package com.duy.ascii.art.bigtext;

import com.duy.ascii.art.utils.FileUtil;

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
                for (int i = 'A'; i <= 'Z' && matcher.find(); i++) {
                    font.put((char) i, matcher.group(2));
                }
                if (matcher.find()) {
                    font.put(' ', matcher.group(2));
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
        ArrayList<String> chars = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String s = hashMap.get(Character.toUpperCase(text.charAt(i)));
            if (s == null) {
                throw new UnsupportedOperationException("Invalid character " + text.charAt(i));
            }
            chars.add(s);
        }
        StringBuilder result = new StringBuilder();
        String[][] maps = new String[chars.size()][chars.get(0).split("\\n").length];
        for (int i = 0; i < chars.size(); i++) {
            maps[i] = chars.get(i).split("\\n");
        }

        for (int j = 0; j < maps[0].length; j++) {
            for (int i = 0; i < chars.size(); i++) {
                result.append(maps[i][j]);
            }
            result.append("\n");
        }


        return result.toString();
    }

}
