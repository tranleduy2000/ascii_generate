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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.asciiart.model.TextArt;

import java.util.ArrayList;

/**
 * Created by Duy on 9/27/2017.
 */

public class TextArtFragment extends SimpleFragment implements TextArtContract.View {
    protected TextArtContract.Presenter mPresenter;
    protected RecyclerView mRecyclerView;
    protected TextArtAdapter mAdapter;
    protected ContentLoadingProgressBar mProgressBar;

    public static TextArtFragment newInstance() {
        Bundle args = new Bundle();
        TextArtFragment fragment = new TextArtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_ascii_art;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new TextArtAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);
        mPresenter = new TextArtPresenter(getContext(), this);
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
    public void display(ArrayList<TextArt> list) {
        mAdapter.clearAll();
        mAdapter.addAll(list);
    }

    @Override
    public void setPresenter(TextArtContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void append(TextArt value) {
        mAdapter.add(value);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }
}
