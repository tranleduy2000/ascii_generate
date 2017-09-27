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

package com.duy.ascii.sharedcode.emoticons;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.duy.ascii.sharedcode.utils.FileUtil;
import com.duy.ascii.sharedcode.asciiart.AsciiArtFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static android.content.ContentValues.TAG;

/**
 * Created by Duy on 03-Jul-17.
 */

 class EmoticonPresenter implements EmoticonContract.Presenter {
    private final Context context;
    private final EmoticonContract.View view;
    private AsyncTask<String, String, ArrayList<String>> loadData;

    public EmoticonPresenter(Context context, EmoticonContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void load(int index) {
        view.showProgress();
        if (loadData != null && !loadData.isCancelled()) {
            loadData.cancel(true);
        }
        loadData = new LoadDataTask(context, new LoadDataTask.Callback() {
            @Override
            public void onResult(ArrayList<String> list) {
                view.hideProgress();
            }
        }, view);
        switch (index) {
            case AsciiArtFragment.INDEX:
                loadData.execute("image.txt");
                break;
        }
    }

    @Override
    public void stop() {
        if (loadData != null) {
            loadData.cancel(true);
        }
    }

    private static class LoadDataTask extends AsyncTask<String, String, ArrayList<String>> {
        private Context context;
        private Callback callback;
        private EmoticonContract.View view;

        LoadDataTask(Context context, Callback callback, EmoticonContract.View view) {
            this.context = context;
            this.callback = callback;
            this.view = view;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            AssetManager assets = context.getAssets();
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
            view.append(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);
            Log.d(TAG, "onPostExecute() called with: list = [" + list + "]");

            if (!isCancelled()) {
                callback.onResult(list);
            }
        }

        interface Callback {
            void onResult(ArrayList<String> list);
        }
    }
}
