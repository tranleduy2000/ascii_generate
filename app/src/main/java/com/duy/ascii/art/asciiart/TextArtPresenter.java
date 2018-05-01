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

package com.duy.ascii.art.asciiart;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.duy.ascii.art.asciiart.model.TextArt;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Duy on 03-Jul-17.
 */
class TextArtPresenter implements TextArtContract.Presenter {
    private final Context mContext;
    private final TextArtContract.View mView;
    private LoadDataTask mLoadDataTask;

    TextArtPresenter(Context context, TextArtContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void onStart() {
        mView.showProgress();
        if (mLoadDataTask != null) {
            mLoadDataTask.cancel(true);
        }
        LoadDataTask.Callback callback = new LoadDataTask.Callback() {
            @Override
            public void onResult(ArrayList<TextArt> list) {
                mView.hideProgress();
            }
        };
        mLoadDataTask = new LoadDataTask(callback, mView);
        try {
            mLoadDataTask.execute(mContext.getAssets().open("new_ascii_art.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        if (mLoadDataTask != null) {
            mLoadDataTask.cancel(true);
        }
    }

    private static class LoadDataTask extends AsyncTask<InputStream, TextArt, ArrayList<TextArt>> {
        private Callback mCallback;
        private TextArtContract.View mView;

        LoadDataTask(Callback callback, TextArtContract.View view) {
            this.mCallback = callback;
            this.mView = view;
        }

        @Override
        protected ArrayList<TextArt> doInBackground(InputStream... params) {
            try {
                InputStream in = params[0];
                String content = IOUtils.toString(in);
                JSONObject jsonObject = new JSONObject(content);
                JSONArray array = jsonObject.getJSONArray(TextArt.KEY_ROOT);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    TextArt textArt = new TextArt();
                    if (item.has("category")) {
                        textArt.setCategory(item.getInt("category"));
                    }
                    if (item.has("time")) {
                        textArt.setTime(item.getLong("time"));
                    }
                    if (item.has("name")) {
                        textArt.setName(item.getString("name"));
                    }
                    textArt.setContent(item.getString("content"));
                    if (item.has("star")) {
                        textArt.setStar(item.getInt("star"));
                    }
                    publishProgress(textArt);
                }
//                Matcher matcher = FileUtil.PATTERN_SPLIT.matcher(string);
//                ArrayList<T> result = new ArrayList<>();
//                while (matcher.find() && !isCancelled()) {
//                    publishProgress(matcher.group(2));
//                }
//                return result;
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(TextArt... values) {
            super.onProgressUpdate(values);
            mView.append(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<TextArt> list) {
            super.onPostExecute(list);
            if (!isCancelled()) {
                mCallback.onResult(list);
            }
        }

        interface Callback {
            void onResult(@Nullable ArrayList<TextArt> list);
        }
    }
}
