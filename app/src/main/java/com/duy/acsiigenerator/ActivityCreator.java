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

package com.duy.acsiigenerator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.duy.acsiigenerator.view.ArtViewGroup;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 09-Jul-17.
 */

public class ActivityCreator extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerHeight;
    Spinner spinnerWidth;
    private Dialog dialog;
    private ArtViewGroup container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        container = findView(R.id.art_view_group);


        findView(R.id.btn_create).setOnClickListener(this);
        container.create(10, 5);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                showDialogCreate();
                break;
        }
    }

    private void showDialogCreate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_create_table);
        builder.setTitle("Create new table");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewTable();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        this.dialog = builder.create();
        this.dialog.show();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.widths));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerWidth = dialog.findViewById(R.id.spinner_width);
        spinnerWidth.setAdapter(adapter);


        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.widths));
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerHeight = dialog.findViewById(R.id.spinner_height);
        spinnerHeight.setAdapter(adapter);
    }

    private void createNewTable() {
        int h = Integer.parseInt(spinnerHeight.getSelectedItem().toString());
        int w = Integer.parseInt(spinnerWidth.getSelectedItem().toString());
        container.create(h, w);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
