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
