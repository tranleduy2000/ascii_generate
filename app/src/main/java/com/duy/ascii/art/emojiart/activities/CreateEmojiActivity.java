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

package com.duy.ascii.art.emojiart.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.duy.ascii.art.BaseActivity;
import com.duy.ascii.sharedcode.R;
import com.duy.ascii.art.emojiart.database.FirebaseHelper;
import com.duy.ascii.art.emojiart.model.EmojiItem;

/**
 * Created by Duy on 9/27/2017.
 */

public class CreateEmojiActivity extends BaseActivity {
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
        mFirebaseHelper.add(new EmojiItem(text, name));
        finish();
    }
}
