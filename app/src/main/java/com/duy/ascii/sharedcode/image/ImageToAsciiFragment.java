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

package com.duy.ascii.sharedcode.image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.utils.ShareUtil;
import com.duy.ascii.sharedcode.SimpleFragment;
import com.duy.ascii.sharedcode.image.converter.AsciiConverter;
import com.duy.ascii.sharedcode.image.gallery.GalleryActivity;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.checkSelfPermission;
import static com.duy.ascii.sharedcode.image.converter.ProcessImageOperation.processImage;

/**
 * Created by Duy on 9/27/2017.
 */

public class ImageToAsciiFragment extends SimpleFragment implements View.OnClickListener {
    private static final int PICK_IMAGE = 1231;
    private static final String TAG = "ImageToAsciiFragment";
    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_PERMISSION = 1002;
    private ImageView mPreview;
    private ProgressBar mProgressBar;
    private Spinner mSpinnerType;
    private File mResultFile = null;
    private Uri mOriginalUri = null;

    public static ImageToAsciiFragment newInstance() {

        Bundle args = new Bundle();

        ImageToAsciiFragment fragment = new ImageToAsciiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_image_to_ascii;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mResultFile = (File) savedInstanceState.getSerializable("result_file");
            mOriginalUri = savedInstanceState.getParcelable("origin_uri");
        }

        mPreview = (ImageView) findViewById(R.id.image_preview);
        findViewById(R.id.btn_select).setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mSpinnerType = (Spinner) findViewById(R.id.spinner_type);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mOriginalUri != null) outState.putParcelable("origin_uri", mOriginalUri);
        if (mResultFile != null) outState.putSerializable("result_file", mResultFile);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_image_to_ascii, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_share:
                shareCurrentImage();
                return true;
            case R.id.action_save:
                saveImage();
                return true;
            case R.id.action_gallery:
                startActivity(new Intent(getContext(), GalleryActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressLint("ObsoleteSdkInt")
    private boolean permissionGrated() {
        int state;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            state = checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (state == PackageManager.PERMISSION_GRANTED) {
                state = checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (state != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission denied, please enable permission", Toast.LENGTH_SHORT).show();
                String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permission, REQUEST_PERMISSION);
                }
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
                if (resultCode == RESULT_OK) {
                    if (intent != null && intent.getData() != null) {
                        mOriginalUri = intent.getData();
                        mPreview.setImageBitmap(null);
                        convertImageToAsciiFromIntent(intent.getData());
                    } else {
                        mOriginalUri = null;
                        mResultFile = null;
                        mPreview.setImageBitmap(null);
                    }
                }
                break;
            case TAKE_PICTURE:
                this.mOriginalUri = intent.getData();
                if (resultCode == RESULT_OK) {
                    if (intent.getData() != null) {
                        convertImageToAsciiFromIntent(intent.getData());
                    } else {
                        Toast.makeText(getContext(), "Capture failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case REQUEST_PERMISSION:
                selectImage();
                break;
        }
    }


    private void convertImageToAsciiFromIntent(Uri uri) {
        this.mResultFile = null;
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
        int i = v.getId();
        if (i == R.id.btn_save) {
            saveImage();
        } else if (i == R.id.btn_select) {
            selectImage();
        } else if (i == R.id.btn_share) {
            shareCurrentImage();
        }
    }

    private void saveImage() {
        if (mResultFile != null) {
            addImageToGallery(mResultFile.getPath());
        } else {
            Toast.makeText(getContext(), R.string.null_uri, Toast.LENGTH_SHORT).show();
        }
    }

    private void shareCurrentImage() {
        if (mResultFile == null) {
            Toast.makeText(getContext(), R.string.null_uri, Toast.LENGTH_SHORT).show();
        } else {
            ShareUtil.shareImage(getContext(), mResultFile);
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


    private class TaskConvertImageToAscii extends AsyncTask<Uri, Void, File> {
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
        protected File doInBackground(Uri... params) {
            try {
                String output = processImage(context, params[0], type);
                if (output != null) {
                    return new File(output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final File uri) {
            super.onPostExecute(uri);
            if (uri == null) {
                Toast.makeText(context, "IO Exception", Toast.LENGTH_SHORT).show();
            } else {
                mPreview.setImageURI(Uri.fromFile(uri));
                mResultFile = uri;
            }
            mProgressBar.setVisibility(View.GONE);
        }

    }
}
