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

package com.duy.ascii.art.asciiart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.duy.ascii.art.BaseActivity;
import com.duy.ascii.art.R;
import com.duy.ascii.art.asciiart.database.FirebaseHelper;
import com.duy.ascii.art.asciiart.model.TextArt;

/**
 * Created by Duy on 9/27/2017.
 */

public class CreateTextArtActivity extends BaseActivity {
    private Button mSubmit;
    private EditText mInput, mName;
    private FirebaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_emoji);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mFirebaseHelper = new FirebaseHelper(this);
        bindView();
    }

    private void bindView() {
        mSubmit = findViewById(R.id.btn_submit);
        mInput = findViewById(R.id.edit_input);
        mName = findViewById(R.id.edit_name);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        String text = mInput.getText().toString();
        if (text.isEmpty()) {
            mInput.setError("Please enter text");
            return;
        }
        String name = mName.getText().toString();
        mFirebaseHelper.add(new TextArt(text, name));
        finish();
    }
}
