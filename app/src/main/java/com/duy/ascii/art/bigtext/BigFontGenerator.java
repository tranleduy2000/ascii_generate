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

import com.duy.ascii.art.database.JsonBridge;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Duy on 07-Jul-17.
 */

public class BigFontGenerator {
    private static final String LATIN_CHAR = "ABCDEF";
    private boolean loaded = false;
    private ArrayList<HashMap<Character, String>> fonts;

    public BigFontGenerator() {
        fonts = new ArrayList<>();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void load(InputStream[] inputStream) {
        for (int i = 0; i < inputStream.length; i++) {
            InputStream stream = inputStream[i];
            try {
                JSONObject json = JsonBridge.getJson(stream);
                HashMap<Character, String> map = new HashMap<>();
                for (char smallText = 'A'; smallText <= 'Z'; smallText++) {
                    String bigText = json.getString(String.valueOf(smallText));
                    map.put(smallText, bigText);
                }
                fonts.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loaded = true;
    }

    public int getSize() {
        return fonts.size();
    }

    /**
     * Convert simple text to big text
     *
     * @param text     - input
     * @param position - font position
     * @return the big text
     */
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
            String str = chars.get(i);
            maps[i] = str.split("\\r?\\n");
        }

        for (int j = 0; j < maps[0].length; j++) {
            for (int i = 0; i < chars.size(); i++) {
                result.append(maps[i][j]);
            }
            if (j != maps[0].length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

}
