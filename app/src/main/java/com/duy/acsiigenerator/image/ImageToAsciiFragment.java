package com.duy.acsiigenerator.image;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.duy.acsiigenerator.image.converter.ProcessImageOperation;

import java.io.File;
import java.io.IOException;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 15-Jun-17.
 */

public class ImageToAsciiFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 1231;
    private static final String TAG = "ImageToAsciiFragment";
    private static final int TAKE_PICTURE = 1;
    private ImageView mPreview;
    private FloatingActionButton mButtonSave;

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
        mPreview = (ImageView) view.findViewById(R.id.image_preview);
        mButtonSave = (FloatingActionButton) view.findViewById(R.id.fab_save);
        mButtonSave.hide();

        view.findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSave.hide();
                selectImage();
            }
        });
        view.findViewById(R.id.btn_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSave.hide();
                takePhoto();
            }
        });
    }

    private void selectImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PICK_IMAGE:
                if (intent != null) {
                    convertImageToAsciiFromIntent(intent);
                }
                break;
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    convertImageToAsciiFromIntent(intent);
                }
                break;
        }
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);
    }


    private void convertImageToAsciiFromIntent(Intent intent) {
        Log.d(TAG, "convertImageToAsciiFromIntent() called with: intent = [" + intent + "]");
        new TaskConvertImageToAscii().execute(intent);
    }

    private class TaskConvertImageToAscii extends AsyncTask<Intent, Void, Uri> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Converting...");
            mProgressDialog.show();
        }

        @Override
        protected Uri doInBackground(Intent... params) {
            final String imagePath;
            try {
                imagePath = ProcessImageOperation.processImage(getContext(), params[0].getData());
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
                Toast.makeText(getContext(), "IO Exception", Toast.LENGTH_SHORT).show();
            } else {
                mPreview.setImageURI(uri);
                mButtonSave.show();
                mButtonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.setType("image/*");
                        startActivity(Intent.createChooser(intent, "Share Image"));
                    }
                });
            }
            mProgressDialog.dismiss();
        }
    }
}
