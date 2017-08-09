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
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.StoreUtil;
import com.duy.ascii.sharedcode.image.converter.AsciiConverter;
import com.duy.ascii.sharedcode.image.gallery.GalleryActivity;

import java.io.File;
import java.io.IOException;

import static com.duy.ascii.sharedcode.image.converter.ProcessImageOperation.processImage;

/**
 * Created by Duy on 09-Aug-17.
 */

public class ImageToAsciiActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE = 1231;
    private static final String TAG = "ImageToAsciiFragment";
    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_PERMISSION = 1002;
    private ImageView mPreview;
    private ProgressBar mProgressBar;
    private Spinner mSpinnerType;
    private Uri mResultUri = null;
    private Uri mOriginalUri = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mResultUri = savedInstanceState.getParcelable("result_uri");
            mOriginalUri = savedInstanceState.getParcelable("origin_uri");
        }
        setContentView(R.layout.activity_image_to_ascii);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.image_to_ascii);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mOriginalUri != null) outState.putParcelable("origin_uri", mOriginalUri);
        if (mResultUri != null) outState.putParcelable("result_uri", mResultUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_to_ascii, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            this.finish();
            return false;
        } else if (i == R.id.action_share) {
            shareCurrentImage();
            return true;
        } else if (i == R.id.action_save) {
            saveImage();
            return false;
        } else if (i == R.id.action_gallery) {
            startActivity(new Intent(this, GalleryActivity.class));
            return false;
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

    private boolean permissionGrated() {
        int state;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            state = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (state == PackageManager.PERMISSION_GRANTED) {
                state = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (state != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied, please enable permission", Toast.LENGTH_SHORT).show();
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
                        mResultUri = null;
                        mPreview.setImageBitmap(null);
                    }
                }
                break;
            case TAKE_PICTURE:
                this.mOriginalUri = intent.getData();
                if (resultCode == Activity.RESULT_OK) {
                    if (intent.getData() != null) {
                        convertImageToAsciiFromIntent(intent.getData());
                    } else {
                        Toast.makeText(this, "Capture failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void convertImageToAsciiFromIntent(Uri uri) {
        this.mResultUri = null;
        new TaskConvertImageToAscii(this, getCurrentType()).execute(uri);
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
        if (mResultUri != null) {
            addImageToGallery(mResultUri.getPath());
        } else {
            Toast.makeText(this, R.string.null_uri, Toast.LENGTH_SHORT).show();
        }
    }

    private void shareCurrentImage() {
        if (mResultUri == null) {
            Toast.makeText(this, R.string.null_uri, Toast.LENGTH_SHORT).show();
        } else {
            StoreUtil.shareImage(this, mResultUri);
        }
    }

    public void addImageToGallery(final String filePath) {
        try {
            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.MediaColumns.DATA, filePath);

            this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Toast.makeText(this, "Saved in gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private class TaskConvertImageToAscii extends AsyncTask<Uri, Void, Uri> {
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
                String output = processImage(context, params[0], type);
                if (output != null) {
                    return Uri.fromFile(new File(output));
                }
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
