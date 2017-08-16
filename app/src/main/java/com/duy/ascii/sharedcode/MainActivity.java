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

package com.duy.ascii.sharedcode;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duy.ascii.sharedcode.asciiart.ImageAsciiActivity;
import com.duy.ascii.sharedcode.bigtext.BigFontActivity;
import com.duy.ascii.sharedcode.emoji.EmojiActivity;
import com.duy.ascii.sharedcode.emoticons.EmoticonsActivity;
import com.duy.ascii.sharedcode.figlet.FigletActivity;
import com.duy.ascii.sharedcode.image.ImageToAsciiActivity;
import com.duy.ascii.sharedcode.unicodesymbol.SymbolActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;


/**
 * Created by Duy on 09-Aug-17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private NativeExpressAdView mAdView;
    private InterstitialAd interstitialAd = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.app_name);

        loadAdView();
        createAdInterstitial();

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
        findViewById(R.id.card_emoji).setOnClickListener(this);
        findViewById(R.id.card_symbol).setOnClickListener(this);
        findViewById(R.id.btn_remove_ads).setOnClickListener(this);

    }

    private void createAdInterstitial() {
        if (!BuildConfig.IS_PREMIUM_USER || hasPremiumApp()) {
            //create ad
            interstitialAd = new InterstitialAd(this);
            if (BuildConfig.DEBUG) {
                interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            } else {
                interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
            }
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    private void loadAdView() {
        if (BuildConfig.IS_PREMIUM_USER || hasPremiumApp()) {
            findViewById(R.id.card_ad_view).setVisibility(View.GONE);
            findViewById(R.id.btn_remove_ads).setVisibility(View.GONE);
            return;
        }
        mAdView = (NativeExpressAdView) findViewById(R.id.native_ad_view);
        if (mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    private boolean hasPremiumApp() {
        if (true) return false;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.duy.asciigenerator.pro", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        switch (v.getId()) {
            case R.id.card_image_to_ascii:
                firebaseAnalytics.logEvent("card_image_to_ascii", new Bundle());
                startActivity(new Intent(this, ImageToAsciiActivity.class));
                break;
            case R.id.card_big_ascii:
                firebaseAnalytics.logEvent("card_big_ascii", new Bundle());
                startActivity(new Intent(this, BigFontActivity.class));
                break;
            case R.id.card_image_ascii:
                firebaseAnalytics.logEvent("card_image_ascii", new Bundle());
                startActivity(new Intent(this, ImageAsciiActivity.class));
                break;
            case R.id.card_emoticons:
                firebaseAnalytics.logEvent("card_emoticons", new Bundle());
                startActivity(new Intent(this, EmoticonsActivity.class));
                break;
            case R.id.card_figlet:
                firebaseAnalytics.logEvent("card_figlet", new Bundle());
                startActivity(new Intent(this, FigletActivity.class));
                break;
            case R.id.card_emoji:
                firebaseAnalytics.logEvent("card_emoji", new Bundle());
                startActivity(new Intent(this, EmojiActivity.class));
                break;
            case R.id.card_symbol:
                firebaseAnalytics.logEvent("card_symbol", new Bundle());
                startActivity(new Intent(this, SymbolActivity.class));
                break;
            case R.id.btn_remove_ads:
                firebaseAnalytics.logEvent("btn_remove_ads", new Bundle());
                StoreUtil.gotoPlayStore(this, "com.duy.asciigenerator.pro");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more_app:
                StoreUtil.moreApp(MainActivity.this);
                return true;
            case R.id.action_rate:
                StoreUtil.gotoPlayStore(MainActivity.this, BuildConfig.APPLICATION_ID);
                return true;
            case R.id.action_share:
                StoreUtil.shareApp(MainActivity.this, BuildConfig.APPLICATION_ID);
                return true;
            case R.id.action_text_converter:
                StoreUtil.gotoPlayStore(MainActivity.this, "duy.com.text_converter");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (BuildConfig.IS_PREMIUM_USER) {
            super.onBackPressed();
            return;
        }
        if (interstitialAd != null) {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        finish();
                    }
                });
            } else {
                super.onBackPressed();
            }
        }
    }
}
