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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.favorite.localdata.TextItem;

import java.util.ArrayList;

/**
 * Created by Duy on 09-Aug-17.
 */

public class FavoriteActivity extends AppCompatActivity implements FavoriteContract.View {
    public static final int INDEX = 2;
    protected FavoriteContract.Presenter mPresenter;
    protected RecyclerView mRecyclerView;
    protected FavoriteAdapter mAdapter;
    protected ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ascii_art);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.ascii_art);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);
        mPresenter = new FavoritePresenter(this, this);

    }


    @Override
    public void showProgress() {
        mProgressBar.show();
    }

    @Override
    public void hideProgress() {
        mProgressBar.hide();
    }

    @Override
    public void display(ArrayList<TextItem> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    @Override
    public void setPresenter(FavoriteContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void append(TextItem value) {
        mAdapter.add(value);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.load(INDEX);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
