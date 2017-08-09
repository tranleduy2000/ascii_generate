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

package com.duy.ascii.sharedcode.image.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.image.converter.AsciiImageWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Duy on 09-Aug-17.
 */

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImage() {
        File file = new File(AsciiImageWriter.PATH_IMAGE);
        if (file.exists()) {
            File[] files = file.listFiles();
            ArrayList<File> paths = new ArrayList<>();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.getPath().endsWith(".png")) {
                        paths.add(f);
                    }
                }
            }
            Collections.sort(paths, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return -Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());
                }
            });
            GalleryAdapter adapter = new GalleryAdapter(this, paths);
            recyclerView.setAdapter(adapter);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }
    }


}
