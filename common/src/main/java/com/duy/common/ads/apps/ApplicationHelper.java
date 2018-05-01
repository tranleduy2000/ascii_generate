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

package com.duy.common.ads.apps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.duy.common.BuildConfig;
import com.duy.common.utils.DLog;
import com.duy.common.utils.StoreUtil;
import com.duy.common.views.viewpager.AutoScrollViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Duy on 26-Dec-17.
 */

public class ApplicationHelper {
    private static final String TAG = "ApplicationHelper";

    @Nullable
    public static ApplicationPagerAdapter setup(AutoScrollViewPager viewPager, FragmentManager fragmentManager, Context context) {
        if (viewPager == null) return null;
        DLog.d(TAG, "setup() called");
        ApplicationPagerAdapter adapter = new ApplicationPagerAdapter(fragmentManager, context);
        viewPager.setAdapter(adapter);
        viewPager.startAutoScroll();
        return adapter;
    }

    @NonNull
    static ArrayList<ApplicationItem> getAllPackage(Context context) {
        return loadData(context, false);
    }

    @NonNull
    private static ArrayList<ApplicationItem> loadData(Context context, boolean includeInstalledApp) {
        includeInstalledApp |= BuildConfig.DEBUG;
        ArrayList<ApplicationItem> items = new ArrayList<>();
        try {
            InputStream stream = context.getAssets().open("application/packages.json");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            String text = new String(byteArray);
            JSONObject jsonObject = new JSONObject(text);
            JSONArray jsonArray = jsonObject.getJSONArray("apps");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject app = jsonArray.getJSONObject(i);
                ApplicationItem applicationItem = new ApplicationItem(
                        app.getString("name"),
                        app.getString("package"),
                        app.getString("icon_url"),
                        app.getString("wall_url"));
                if (StoreUtil.isAppInstalled(context, applicationItem.getApplicationId())) {
                    if (includeInstalledApp) {
                        items.add(applicationItem);
                    }
                } else {
                    items.add(applicationItem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DLog.d(TAG, "loadData: " + items);
        return items;
    }
}
