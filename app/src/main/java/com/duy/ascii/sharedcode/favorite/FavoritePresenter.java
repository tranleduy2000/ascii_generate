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

package com.duy.ascii.sharedcode.favorite;

import android.content.Context;
import android.os.AsyncTask;

import com.duy.ascii.sharedcode.favorite.localdata.DatabaseHelper;
import com.duy.ascii.sharedcode.favorite.localdata.TextItem;

import java.util.ArrayList;

/**
 * Created by Duy on 03-Jul-17.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {
    private final Context context;
    private final FavoriteContract.View view;
    private AsyncTask<String, String, ArrayList<TextItem>> mLoadData;
    private DatabaseHelper mDatabaseHelper;

    public FavoritePresenter(Context context, FavoriteContract.View view) {
        this.context = context;
        this.view = view;
        this.mDatabaseHelper = new DatabaseHelper(context);
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    @Override
    public void load(int index) {
        view.showProgress();
        if (mLoadData != null && !mLoadData.isCancelled()) {
            mLoadData.cancel(true);
        }
        mLoadData = new LoadDataTask(new LoadDataTask.Callback() {
            @Override
            public void onResult(ArrayList<TextItem> list) {
                view.hideProgress();
                view.display(list);
            }
        }, view, mDatabaseHelper);
        mLoadData.execute();
    }

    @Override
    public void stop() {
        if (mLoadData != null) {
            mLoadData.cancel(true);
        }
    }

    private static class LoadDataTask extends AsyncTask<String, String, ArrayList<TextItem>> {
        private Callback callback;
        private FavoriteContract.View view;
        private DatabaseHelper mDatabaseHelper;

        LoadDataTask(Callback callback, FavoriteContract.View view, DatabaseHelper mDatabaseHelper) {
            this.callback = callback;
            this.view = view;
            this.mDatabaseHelper = mDatabaseHelper;
        }

        @Override
        protected ArrayList<TextItem> doInBackground(String... params) {
            return mDatabaseHelper.getAll();
        }


        @Override
        protected void onPostExecute(ArrayList<TextItem> list) {
            super.onPostExecute(list);
            if (!isCancelled()) {
                callback.onResult(list);
            }
        }

        interface Callback {
            void onResult(ArrayList<TextItem> list);
        }
    }
}
