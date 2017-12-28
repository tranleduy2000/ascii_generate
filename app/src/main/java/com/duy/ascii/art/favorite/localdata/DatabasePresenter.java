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

package com.duy.ascii.art.favorite.localdata;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Duy on 09-Jul-17.
 */

public class DatabasePresenter implements DatabaseContract.Presenter {
    private Context context;
    @Nullable
    private DatabaseContract.View view;
    private DatabaseHelper mDatabaseHelper;

    public DatabasePresenter(Context context, @Nullable DatabaseContract.View view) {
        this.context = context;
        this.view = view;
        mDatabaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void update(TextItem item) {
    }

    @Override
    public void delete(TextItem item) {
        mDatabaseHelper.delete(item);
    }

    @Override
    public void insert(TextItem item) {
        mDatabaseHelper.insert(item);
    }

}
