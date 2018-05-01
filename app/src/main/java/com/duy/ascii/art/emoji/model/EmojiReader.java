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

package com.duy.ascii.art.emoji.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.os.Build;

import com.duy.ascii.art.database.JsonBridge;
import com.duy.common.utils.DLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
            JSONObject jsonObject = JsonBridge.getJson(assets, "emoji/" + fileName);
            String title = jsonObject.getString(EmojiCategory.TITLE);
            String desc = jsonObject.getString(EmojiCategory.DESCRIPTION);
            EmojiCategory category = new EmojiCategory(title, desc);

            JSONArray jsonArray = jsonObject.getJSONArray(EmojiCategory.DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject emoji = jsonArray.getJSONObject(i);
                String emojiChar = emoji.getString(EmojiItem.CHARACTER);
                String emojiDesc = emoji.getString(EmojiItem.DESCRIPTION);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (paint.hasGlyph(emojiChar)) {
                        category.add(new EmojiItem(emojiChar, emojiDesc));
                    }
                } else if (emojiChar.length() <= 3) {
                    category.add(new EmojiItem(emojiChar, emojiDesc));
                }
            }
            emojiList.add(category);
        }
        return emojiList;
    }
}
