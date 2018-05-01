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
