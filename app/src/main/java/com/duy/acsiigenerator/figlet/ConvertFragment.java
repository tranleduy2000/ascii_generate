package com.duy.acsiigenerator.figlet;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duy.acsiigenerator.figlet.adapter.ResultAdapter;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import imagetotext.duy.com.asciigenerator.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Duy on 15-Jun-17.
 */

public class ConvertFragment extends Fragment implements ConvertContract.View, ResultAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Dialog dialog;
    private ResultAdapter mAdapter;
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

    public static ConvertFragment newInstance() {

        Bundle args = new Bundle();

        ConvertFragment fragment = new ConvertFragment();
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
        mProgressBar.setProgress(0);
    }

    @Override
    public int getMaxProgress() {
        return mProgressBar.getMax();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_figlet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditIn = (EditText) view.findViewById(R.id.edit_in);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ResultAdapter(getContext(), view.findViewById(R.id.empty_view));
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mEditIn.addTextChangedListener(mInputTextWatcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditIn.setText(sharedPreferences.getString("key_save", ""));
    }

    @Override
    public void onDestroyView() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString("key_save", mEditIn.getText().toString()).apply();
        super.onDestroyView();
    }


    private void showDialogChooseColor() {
        AlertDialog build = ColorPickerDialogBuilder.with(getContext()).setOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int i) {
                mAdapter.setColor(i);
            }
        }).build();
        this.dialog = build;
        build.show();
    }


    @Override
    public void onSaveImage(final Bitmap bitmap) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.save_image);
        builder.setView(R.layout.dialog_save_file);
        this.dialog = builder.create();
        dialog.show();

        final String fileName = Integer.toHexString((int) System.currentTimeMillis()) + ".png";
        final EditText editName = (EditText) dialog.findViewById(R.id.edit_name);
        editName.setText(fileName);

        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty()) {
                    editName.setError(getString(R.string.enter_file_name));
                    return;
                }
                writeToStorage(bitmap, getContext().getFilesDir().getPath(), editName.getText().toString());
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.btn_save_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty()) {
                    editName.setError(getString(R.string.enter_file_name));
                    return;
                }

                String uri = writeToStorage(bitmap, getContext().getFilesDir().getPath(), editName.getText().toString());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share Image"));
                dialog.cancel();
            }
        });
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
