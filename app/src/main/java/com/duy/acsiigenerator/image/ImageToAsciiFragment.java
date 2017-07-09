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

package com.duy.acsiigenerator.image;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.duy.acsiigenerator.image.converter.AsciiConverter;
import com.duy.acsiigenerator.image.converter.ProcessImageOperation;

import java.io.File;
import java.io.IOException;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 15-Jun-17.
 */

public class ImageToAsciiFragment extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE = 1231;
    private static final String TAG = "ImageToAsciiFragment";
    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_PERMISSION = 1002;
    private ImageView mPreview;
    private ProgressBar mProgressBar;
    private Spinner mSpinnerType;
    private Uri mResultUri = null;
    private Uri mOriginalUri = null;

    public static ImageToAsciiFragment newInstance() {
        Bundle args = new Bundle();

        ImageToAsciiFragment fragment = new ImageToAsciiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPreview = view.findViewById(R.id.image_preview);

        view.findViewById(R.id.btn_save).setOnClickListener(this);
        view.findViewById(R.id.btn_share).setOnClickListener(this);
        view.findViewById(R.id.btn_select).setOnClickListener(this);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mSpinnerType = view.findViewById(R.id.spinner_type);
        mSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mOriginalUri != null) {
                    convertImageToAsciiFromIntent(mOriginalUri);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void selectImage() {
        if (!permissionGrated()) {
            return;
        }

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private boolean permissionGrated() {
        int state;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            state = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (state == PackageManager.PERMISSION_GRANTED) {
                state = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (state != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission denied, please enable permission", Toast.LENGTH_SHORT).show();
                String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PICK_IMAGE:
                if (intent != null) {
                    this.mOriginalUri = intent.getData();
                    convertImageToAsciiFromIntent(intent.getData());
                } else {
                    mOriginalUri = null;
                }
                break;
            case TAKE_PICTURE:
                this.mOriginalUri = intent.getData();
                if (resultCode == Activity.RESULT_OK) {
                    if (intent.getData() != null) {
                        convertImageToAsciiFromIntent(intent.getData());
                    } else {
                        Toast.makeText(getContext(), "Capture failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void convertImageToAsciiFromIntent(Uri uri) {
        this.mResultUri = null;
        new TaskConvertImageToAscii(getContext(), getCurrentType()).execute(uri);
    }

    private AsciiConverter.ColorType getCurrentType() {
        switch (mSpinnerType.getSelectedItemPosition()) {
            case 0:
                return AsciiConverter.ColorType.ANSI_COLOR;
            case 1:
                return AsciiConverter.ColorType.FULL_COLOR;
            case 2:
                return AsciiConverter.ColorType.NONE;
            default:
                return AsciiConverter.ColorType.ANSI_COLOR;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save: {
                if (mResultUri != null) {
                    addImageToGallery(mResultUri.getPath());
                } else {
                    Toast.makeText(getContext(), R.string.null_uri, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_select: {
                selectImage();
                break;
            }
            case R.id.btn_share: {
                if (mResultUri == null) {
                    Toast.makeText(getContext(), R.string.null_uri, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, mResultUri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share Image"));
                }
                break;
            }
        }
    }

    public void addImageToGallery(final String filePath) {
        try {
            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.MediaColumns.DATA, filePath);

            getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Toast.makeText(getContext(), "Saved in gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private class TaskConvertImageToAscii extends AsyncTask<Uri, Void, Uri> {
        private String textPath;
        private String imagePath;
        private Context context;
        private AsciiConverter.ColorType type;

        TaskConvertImageToAscii(Context context, AsciiConverter.ColorType type) {
            this.context = context;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Uri doInBackground(Uri... params) {
            try {
                Pair<String, String> output = ProcessImageOperation.processImage(context,
                        params[0], type);
                imagePath = output.first;
                textPath = output.second;
                return Uri.fromFile(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Uri uri) {
            super.onPostExecute(uri);
            if (uri == null) {
                Toast.makeText(context, "IO Exception", Toast.LENGTH_SHORT).show();
            } else {
                mPreview.setImageURI(uri);
                mResultUri = uri;
            }
            mProgressBar.setVisibility(View.GONE);

        }

    }
}
