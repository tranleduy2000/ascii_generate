package com.duy.acsiigenerator.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duy.acsiigenerator.image.converter.ProcessImageOperation;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 15-Jun-17.
 */

public class ImageToAsciiFragment extends Fragment {
    private static final int PICK_IMAGE = 1231;
    private PhotoView mPreview;

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
        mPreview = (PhotoView) view.findViewById(R.id.image_preview);
        view.findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
                    final String imagePath;
                    try {
                        imagePath = new ProcessImageOperation().
                                processImage(getContext(), intent.getData());
                        Uri uri = Uri.fromFile(new File(imagePath));
                        mPreview.setImageURI(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }
}
