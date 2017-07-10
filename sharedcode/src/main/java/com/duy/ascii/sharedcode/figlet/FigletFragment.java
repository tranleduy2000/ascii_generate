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

package com.duy.ascii.sharedcode.figlet;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.duy.ascii.sharedcode.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Duy on 15-Jun-17.
 */

public class FigletFragment extends Fragment implements ConvertContract.View, FigletAdapter.OnItemClickListener {
    private static final String TAG = "FigletFragment";
    private ContentLoadingProgressBar mProgressBar;
    private Dialog dialog;
    private FigletAdapter mAdapter;
    @Nullable
    private ConvertContract.Presenter mPresenter;
    private EditText mEditIn;
    private TextWatcher mInputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mPresenter != null) {
                mPresenter.onTextChanged(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public static FigletFragment newInstance() {

        Bundle args = new Bundle();

        FigletFragment fragment = new FigletFragment();
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
    public void setPresenter(@Nullable ConvertContract.Presenter presenter) {
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

        RecyclerView mRecyclerView = view.findViewById(R.id.listview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FigletAdapter(getActivity(), view.findViewById(R.id.empty_view));
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mEditIn.addTextChangedListener(mInputTextWatcher);

        if (mPresenter == null) {
            mPresenter = new FigletPresenter(getContext().getAssets(), this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditIn.setText(sharedPreferences.getString(TAG, ""));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroyView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString(TAG, mEditIn.getText().toString()).apply();
        mPresenter.cancel();
        super.onDestroyView();
    }


    @Override
    public void onSaveImage(final Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission denied, please enable permission", Toast.LENGTH_SHORT).show();
               requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                bitmap.recycle();
                return;
            }
        }
        final String fileName = System.currentTimeMillis() + ".png";
        String uri = writeToStorage(bitmap, getContext().getFilesDir().getPath(), fileName);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share Image"));
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
