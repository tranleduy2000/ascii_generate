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

package com.duy.ascii.art.asciiart;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.duy.ascii.art.utils.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by Duy on 03-Jul-17.
 */
class AsciiArtPresenter implements AsciiArtContract.Presenter {
    private final Context context;
    private final AsciiArtContract.View mView;
    private AsyncTask<String, String, ArrayList<String>> mLoadDataTask;

    AsciiArtPresenter(Context context, AsciiArtContract.View view) {
        this.context = context;
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
            public void onResult(ArrayList<String> list) {
                mView.hideProgress();
            }
        };
        mLoadDataTask = new LoadDataTask(context, callback, mView);
        mLoadDataTask.execute("image.txt");
    }

    @Override
    public void onStop() {
        if (mLoadDataTask != null) {
            mLoadDataTask.cancel(true);
        }
    }

    private static class LoadDataTask extends AsyncTask<String, String, ArrayList<String>> {
        private Context mContext;
        private Callback mCallback;
        private AsciiArtContract.View mView;

        LoadDataTask(Context context, Callback callback, AsciiArtContract.View view) {
            this.mContext = context;
            this.mCallback = callback;
            this.mView = view;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            AssetManager assets = mContext.getAssets();
            try {
                InputStream stream = assets.open(params[0]);
                String string = FileUtil.streamToString(stream);
                Matcher matcher = FileUtil.PATTERN_SLIP.matcher(string);
                ArrayList<String> result = new ArrayList<>();
                while (matcher.find() && !isCancelled()) {
                    publishProgress(matcher.group(2));
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mView.append(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);
            if (!isCancelled()) {
                mCallback.onResult(list);
            }
        }

        interface Callback {
            void onResult(ArrayList<String> list);
        }
    }
}
