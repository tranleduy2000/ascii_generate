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

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duy.ascii.sharedcode.asciiart.ImageAsciiActivity;
import com.duy.ascii.sharedcode.bigtext.BigFontActivity;
import com.duy.ascii.sharedcode.emoticons.EmoticonsActivity;
import com.duy.ascii.sharedcode.figlet.FigletActivity;
import com.duy.ascii.sharedcode.image.ImageToAsciiActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 09-Aug-17.
 */

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private NativeExpressAdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.app_name);

        loadAdView();

        Glide.with(this)
                .load(R.drawable.header_image_to_ascii)
                .apply(new RequestOptions().centerCrop())
                .into((ImageView) findViewById(R.id.header_ascii));
        ((TextView) findViewById(R.id.header_figlet)).setTypeface(Typeface.MONOSPACE);

        findViewById(R.id.card_emoticons).setOnClickListener(this);
        findViewById(R.id.card_image_to_ascii).setOnClickListener(this);
        findViewById(R.id.card_big_ascii).setOnClickListener(this);
        findViewById(R.id.card_figlet).setOnClickListener(this);
        findViewById(R.id.card_image_ascii).setOnClickListener(this);
    }

    private void loadAdView() {
        mAdView = (NativeExpressAdView) findViewById(R.id.ad_view);
        if (mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) mAdView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) mAdView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_image_to_ascii:
                startActivity(new Intent(this, ImageToAsciiActivity.class));
                break;
            case R.id.card_big_ascii:
                startActivity(new Intent(this, BigFontActivity.class));
                break;
            case R.id.card_image_ascii:
                startActivity(new Intent(this, ImageAsciiActivity.class));
                break;
            case R.id.card_emoticons:
                startActivity(new Intent(this, EmoticonsActivity.class));
                break;
            case R.id.card_figlet:
                startActivity(new Intent(this, FigletActivity.class));
                break;
        }
    }
}
