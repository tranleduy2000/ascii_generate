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

package com.duy.ascii.art;

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

import com.duy.ascii.art.view.ArtViewGroup;


/**
 * Created by Duy on 09-Jul-17.
 */

public class ActivityCreator extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerHeight;
    private Spinner spinnerWidth;
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
        int i = v.getId();
        if (i == R.id.btn_create) {
            showDialogCreate();
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
