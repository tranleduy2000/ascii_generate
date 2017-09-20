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

package com.duy.ascii.sharedcode.unicodesymbol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.ascii.sharedcode.AdBannerActivity;
import com.duy.ascii.sharedcode.FileUtil;
import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.ShareUtil;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompat;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.sharedcode.emoji.EmojiAdapter;
import com.duy.ascii.sharedcode.emoji.HeaderAdapter;
import com.duy.ascii.sharedcode.favorite.localdata.DatabasePresenter;
import com.duy.ascii.sharedcode.favorite.localdata.TextItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Duy on 09-Aug-17.
 */

public class SymbolActivity extends AdBannerActivity {
    private ArrayList<String> symbols = new ArrayList<>();
    private EditText mEditInput;
    private DatabasePresenter mDatabasePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symbol);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.cool_symbol);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mDatabasePresenter = new DatabasePresenter(this, null);

        parseData();

        mEditInput = (EditText) findViewById(R.id.edit_input);
        Button btnCopy = (Button) findViewById(R.id.btn_copy);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        EmojiAdapter emojiAdapter = new EmojiAdapter(this, symbols);
        recyclerView.setAdapter(emojiAdapter);

        emojiAdapter.setListener(new HeaderAdapter.EmojiClickListener() {
            @Override
            public void onClick(String emoji) {
                mEditInput.getEditableText().insert(Math.max(mEditInput.getSelectionStart(), 0), emoji);
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManagerCompat manager = ClipboardManagerCompatFactory.getManager(SymbolActivity.this);
                manager.setText(mEditInput.getText());
                Toast.makeText(SymbolActivity.this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(mEditInput.getText().toString(), SymbolActivity.this);
            }
        });

        findViewById(R.id.img_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditInput.getText().toString().isEmpty()) {
                    mDatabasePresenter.insert(new TextItem(mEditInput.getText().toString()));
                    Toast.makeText(SymbolActivity.this, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void parseData() {
        try {
            InputStream open = getAssets().open("symbol.txt");
            String string = FileUtil.streamToString(open);
            String[] split = string.split("\\s+");
            Collections.addAll(symbols, split);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
