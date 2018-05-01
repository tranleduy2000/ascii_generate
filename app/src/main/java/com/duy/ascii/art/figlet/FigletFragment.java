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

package com.duy.ascii.art.figlet;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.ascii.art.utils.ShareUtil;
import com.duy.ascii.art.R;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Duy on 15-Jun-17.
 */

public class FigletFragment extends Fragment implements FigletContract.View, FigletAdapter.OnItemClickListener {
    private static final String TAG = "FigletFragment";
    private ContentLoadingProgressBar mProgressBar;
    private Dialog dialog;
    private FigletAdapter mAdapter;
    @Nullable
    private FigletContract.Presenter mPresenter;
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
    public void setPresenter(@Nullable FigletContract.Presenter presenter) {
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
        return inflater.inflate(R.layout.fragment_figlet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mEditIn = view.findViewById(R.id.edit_in);

        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(false);

        RecyclerView mRecyclerView = view.findViewById(R.id.listview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FigletAdapter(getContext(), view.findViewById(R.id.empty_view));
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
    public void onDestroyView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString(TAG, mEditIn.getText().toString()).apply();
        if (mPresenter != null) {
            mPresenter.cancel();
        }
        super.onDestroyView();
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission denied, please enable permission", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onShareImage(@NonNull File bitmap) {
        ShareUtil.shareImage(getContext(), bitmap);
    }

    @Override
    public void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_figlet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_background_color:
                selectBackgroundColor();
                break;
            case R.id.action_text_color:
                selectTextColor();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectTextColor() {
        ColorPickerDialogBuilder.with(getContext())
                .setPositiveButton("Select", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int color, Integer[] integers) {
                        mAdapter.setTextColor(color);
                    }
                })
                .initialColor(mAdapter.getTextColor())
                .build()
                .show();
    }

    private void selectBackgroundColor() {
        ColorPickerDialogBuilder.with(getContext())
                .setPositiveButton("Select", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int color, Integer[] integers) {
                        mAdapter.setBackgroundColor(color);
                    }
                })
                .initialColor(mAdapter.getBackgroundColor())
                .build()
                .show();
    }
}
