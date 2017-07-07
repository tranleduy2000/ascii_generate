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

package com.duy.acsiigenerator.emoticons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.acsiigenerator.emoticons.EmoticonContract;
import com.duy.acsiigenerator.emoticons.EmoticonPresenter;
import com.duy.acsiigenerator.emoticons.ShowAdapter;

import java.util.ArrayList;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 03-Jul-17.
 */

public abstract class BaseFragment extends Fragment implements EmoticonContract.View {

    protected EmoticonContract.Presenter mPresenter;
    protected RecyclerView mRecyclerView;
    protected ShowAdapter mFacesAdapter;
    protected ContentLoadingProgressBar mProgressBar;

    @Override
    public void showProgress() {
        mProgressBar.show();
    }

    @Override
    public void hideProgress() {
        mProgressBar.hide();
    }

    @Override
    public void display(ArrayList<String> list) {
        mFacesAdapter.clear();
        mFacesAdapter.addAll(list);
    }

    @Override
    public void setPresenter(EmoticonContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void append(String value) {
        mFacesAdapter.add(value);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start(getIndex());
    }

    protected abstract int getIndex();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_emoticon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter == null) {
            mPresenter = new EmoticonPresenter(getContext(), this);
        }
    }
}
