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

package com.duy.ascii.art.emoji.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.os.Build;

import com.duy.ascii.art.utils.FileUtil;
import com.duy.common.utils.DLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Duy on 1/11/2018.
 */

public class EmojiReader {
    private static final String TAG = "EmojiReader";

    public static ArrayList<EmojiCategory> readData(Context context) throws IOException, JSONException {
        if (DLog.DEBUG) DLog.d(TAG, "readData() called");
        //the result
        ArrayList<EmojiCategory> emojiList = new ArrayList<>();

        AssetManager assets = context.getAssets();
        String[] fileNames = assets.list("emoji");

        Paint paint = new Paint();

        for (String fileName : fileNames) {
            InputStream stream = assets.open("emoji/" + fileName);
            String jsonStr = FileUtil.streamToString(stream);
            JSONObject jsonObject = new JSONObject(jsonStr);
            String title = jsonObject.getString(EmojiCategory.TITLE);
            String desc = jsonObject.getString(EmojiCategory.DESCRIPTION);
            EmojiCategory category = new EmojiCategory(title, desc);

            JSONArray jsonArray = jsonObject.getJSONArray(EmojiCategory.DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject emoji = jsonArray.getJSONObject(i);
                String emojiChar = emoji.getString(EmojiItem.CHARACTER);
                String emojiDesc = emoji.getString(EmojiItem.DESCRIPTION);
                if (emojiChar.length() == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (paint.hasGlyph(emojiChar)) {
                            category.add(new EmojiItem(emojiChar, emojiDesc));
                        }
                    } else {
                        category.add(new EmojiItem(emojiChar, emojiDesc));
                    }
                }
            }
            emojiList.add(category);
        }
        return emojiList;
    }
}
