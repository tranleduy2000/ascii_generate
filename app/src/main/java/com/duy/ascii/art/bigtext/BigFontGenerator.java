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

public class BigFontGenerator {
    private boolean loaded = false;
    private ArrayList<HashMap<Character, String>> fonts;

    public BigFontGenerator() {
        fonts = new ArrayList<>();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void load(InputStream[] inputStream) {
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

        //prepare all characters
        HashMap<Character, String> font = fonts.get(position);
        ArrayList<String> characters = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String cChar = font.get(Character.toUpperCase(text.charAt(i)));
            if (cChar == null) {
                throw new UnsupportedOperationException("Invalid character " + text.charAt(i));
            }
            characters.add(cChar);
        }

        //make result
        StringBuilder result = new StringBuilder();
        int row = characters.size();
        int column = characters.get(0).split("\\n").length;

        String[][] data = new String[row][column];
        for (int i = 0; i < row; i++) {
            data[i] = characters.get(i).split("\\n");
        }

        for (int j = 0; j < column; j++) {
            for (int i = 0; i < row; i++) {
                result.append(data[i][j]);
            }
            if (j != column - 1) result.append("\n");
        }

        return result.toString();
    }

}
