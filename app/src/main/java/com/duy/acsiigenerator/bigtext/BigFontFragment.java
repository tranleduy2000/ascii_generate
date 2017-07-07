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

package com.duy.acsiigenerator.bigtext;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.acsiigenerator.figlet.adapter.ResultAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 07-Jul-17.
 */

public class BigFontFragment extends Fragment implements BigFontContract.View {
    private RecyclerView mRecyclerView;
    private ContentLoadingProgressBar mProgressBar;
    private Dialog dialog;
    private ResultAdapter mAdapter;
    @Nullable
    private BigFontContract.Presenter mPresenter;
    private EditText mEditIn;
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
        mAdapter.setColor(color);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_figlet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditIn = view.findViewById(R.id.edit_in);

        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(false);

        mRecyclerView = view.findViewById(R.id.listview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ResultAdapter(getActivity(), view.findViewById(R.id.empty_view));
        mRecyclerView.setAdapter(mAdapter);
        mEditIn.addTextChangedListener(mInputTextWatcher);

        createPresenter();
    }

    private void createPresenter() {
        try {
            AssetManager assets = getContext().getAssets();
            String[] paths = assets.list("bigtext");
            InputStream[] inputStreams = new InputStream[paths.length];
            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                inputStreams[i] = assets.open(path);
            }
            mPresenter = new BigFontPresenter(inputStreams, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditIn.setText(sharedPreferences.getString("key_save", ""));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroyView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString("key_save", mEditIn.getText().toString()).apply();
        super.onDestroyView();
    }



    private String writeToStorage(Bitmap bitmap, String path, String fileName) {
        File file = new File(path, fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
            Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
            try {
                if (out != null) {
                    out.close();
                }
                return MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                        file.getAbsolutePath(), file.getName(), file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

}
