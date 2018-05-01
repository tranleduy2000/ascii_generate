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

package com.duy.ascii.art.favorite;

import android.content.Context;
import android.os.AsyncTask;

import com.duy.ascii.art.favorite.localdata.DatabaseHelper;
import com.duy.ascii.art.favorite.localdata.TextItem;

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
    public void loadData() {
        view.showProgress();
        if (mLoadData != null && !mLoadData.isCancelled()) {
            mLoadData.cancel(true);
        }
        LoadDataTask.Callback finish = new LoadDataTask.Callback() {
            @Override
            public void onResult(ArrayList<TextItem> list) {
                view.hideProgress();
                view.display(list);
            }
        };
        mLoadData = new LoadDataTask(finish, view, mDatabaseHelper);
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
