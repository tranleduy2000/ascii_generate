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

package com.duy.ascii.art.image.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.duy.ascii.art.utils.FileUtil;
import com.duy.ascii.art.R;

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
        File folder = (FileUtil.getImageDirectory(this));
        if (folder.exists()) {
            File[] files = folder.listFiles();
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
