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

package com.duy.ascii.art.database;

import android.content.res.AssetManager;
import android.util.Base64;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Duy on 1/11/2018.
 */

public class JsonBridge {
    private static final char key = 10;

    public static JSONObject getJson(AssetManager assetManager, String path) throws JSONException, IOException {
        InputStream stream = assetManager.open(path);
        return getJson(stream);
    }

    public static JSONObject getJson(InputStream stream) throws JSONException, IOException {
        byte[] data = IOUtils.toByteArray(stream);
        byte[] decode = decode(data);
        String content = new String(decode);
        return new JSONObject(content);
    }

    private static byte[] decode(byte[] content) {
//        return Base64.decode(content, Base64.DEFAULT);
        // TODO: 1/11/2018 encode before publish
        return content;
    }

    public static byte[] encode(String content) {
        return Base64.encode(content.getBytes(), Base64.DEFAULT);
    }

}
