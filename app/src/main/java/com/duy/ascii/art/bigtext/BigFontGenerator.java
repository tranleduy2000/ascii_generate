/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
