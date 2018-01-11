/*
 * Copyright (c) 2018 by Tran Le Duy
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
    public static JSONObject getJson(AssetManager assetManager, String path) throws JSONException, IOException {
        InputStream stream = assetManager.open(path);

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
