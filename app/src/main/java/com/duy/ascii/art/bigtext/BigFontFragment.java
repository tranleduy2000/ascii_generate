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

package com.duy.ascii.art.bigtext;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.duy.ascii.art.R;

import java.io.InputStream;
import java.util.ArrayList;


public class BigFontFragment extends Fragment implements BigFontContract.View {
    private static final String TAG = "BigFontFragment";

    private ContentLoadingProgressBar mProgressBar;
    private BigFontAdapter mAdapter;
    private BigFontContract.Presenter mPresenter;
    private EditText mEditInput;
    private TextWatcher mInputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mPresenter != null) {
                mPresenter.convert(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public static BigFontFragment newInstance() {

        Bundle args = new Bundle();

        BigFontFragment fragment = new BigFontFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void showResult(@NonNull ArrayList<String> result) {

    }

    @Override
    public void clearResult() {
        mAdapter.clear();
    }

    @Override
    public void addResult(String text) {
        mAdapter.add(text);
    }

    @Override
    public void setPresenter(@Nullable BigFontContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setProgress(int process) {
        mProgressBar.setProgress(process);
    }

    @Override
    public int getMaxProgress() {
        return mProgressBar.getMax();
    }

    @Override
    public void setColor(int color) {
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bigfont, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditInput = view.findViewById(R.id.edit_in);

        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(false);

        RecyclerView recyclerView = view.findViewById(R.id.listview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new BigFontAdapter(getContext(), view.findViewById(R.id.empty_view));
        recyclerView.setAdapter(mAdapter);

        mEditInput.addTextChangedListener(mInputTextWatcher);
        createPresenter();
    }

    private void createPresenter() {
        if (mPresenter != null) return;
        try {
            AssetManager assets = getContext().getAssets();
            String[] names = assets.list("bigtext_json");
            InputStream[] inputStreams = new InputStream[names.length];
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                inputStreams[i] = assets.open("bigtext_json/" + name);
            }
            mPresenter = new BigFontPresenter(inputStreams, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditInput.setText(sharedPreferences.getString(TAG, ""));
        super.onResume();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString(TAG, mEditInput.getText().toString()).apply();
        if (mPresenter != null) {
            mPresenter.cancel();
        }
    }

}
