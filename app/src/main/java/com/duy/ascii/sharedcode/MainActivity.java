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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.duy.ascii.sharedcode.asciiart.AsciiArtFragment;
import com.duy.ascii.sharedcode.bigtext.BigFontFragment;
import com.duy.ascii.sharedcode.emoji.CategoriesEmojiFragment;
import com.duy.ascii.sharedcode.emojiart.EmojiArtFragment;
import com.duy.ascii.sharedcode.emojiart.fragments.RecentFragment;
import com.duy.ascii.sharedcode.emoticons.EmoticonsFragment;
import com.duy.ascii.sharedcode.favorite.FavoriteActivity;
import com.duy.ascii.sharedcode.figlet.FigletFragment;
import com.duy.ascii.sharedcode.image.ImageToAsciiFragment;
import com.duy.ascii.sharedcode.unicodesymbol.SymbolFragment;
import com.duy.ascii.sharedcode.utils.StoreUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;


/**
 * Created by Duy on 09-Aug-17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private NativeExpressAdView mAdView;
    private InterstitialAd mInterstitialAd = null;
    private ViewGroup mContainerAd;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(R.string.app_name);

        bindView();
        addEvent();

//        loadAdView();
//        Glide.with(this)
//                .load(R.drawable.header_image_to_ascii)
//                .apply(new RequestOptions().centerCrop())
//                .into((ImageView) findViewById(R.id.header_ascii));
//        ((TextView) findViewById(R.id.header_figlet)).setTypeface(Typeface.MONOSPACE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.content, RecentFragment.newInstance())
                .commit();
    }

    private void addEvent() {
//        findViewById(R.id.card_emoticons).setOnClickListener(this);
//        findViewById(R.id.card_image_to_ascii).setOnClickListener(this);
//        findViewById(R.id.card_big_ascii).setOnClickListener(this);
//        findViewById(R.id.card_figlet).setOnClickListener(this);
//        findViewById(R.id.card_image_ascii).setOnClickListener(this);
//        findViewById(R.id.card_emoji).setOnClickListener(this);
//        findViewById(R.id.card_symbol).setOnClickListener(this);
//        findViewById(R.id.btn_remove_ads).setOnClickListener(this);
    }

    private void bindView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void loadAdView() {
        if (BuildConfig.IS_PREMIUM_USER || hasPremiumApp()) {
            findViewById(R.id.card_ad_view).setVisibility(View.GONE);
            findViewById(R.id.btn_remove_ads).setVisibility(View.GONE);
        } else {
            mContainerAd = (ViewGroup) findViewById(R.id.container_ad);
            mContainerAd.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContainerAd.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    createNativeAdView();
                }
            });
        }

    }

    private void createNativeAdView() {
        if (mContainerAd != null) {
            mAdView = new NativeExpressAdView(this);
            mAdView.setAdUnitId(getString(R.string.ad_unit_main));
            int width = (int) (mContainerAd.getWidth() / getResources().getDisplayMetrics().scaledDensity);
            mAdView.setAdSize(new AdSize(width, 250));
            mContainerAd.removeAllViews();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            mContainerAd.addView(mAdView, params);
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    private boolean hasPremiumApp() {
        return false;
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
            case R.id.action_favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (BuildConfig.IS_PREMIUM_USER) {
            super.onBackPressed();
            return;
        }
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        finish();
                    }
                });
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_ascii_art:
                fragmentTransaction.replace(R.id.content, AsciiArtFragment.newInstance()).commit();
                break;
            case R.id.action_big_text:
                fragmentTransaction.replace(R.id.content, BigFontFragment.newInstance()).commit();
                break;
            case R.id.action_image_to_ascii:
                fragmentTransaction.replace(R.id.content, ImageToAsciiFragment.newInstance()).commit();
                break;
            case R.id.action_emoji:
                fragmentTransaction.replace(R.id.content, CategoriesEmojiFragment.newInstance()).commit();
                break;
            case R.id.action_emoji_art:
                fragmentTransaction.replace(R.id.content, EmojiArtFragment.newInstance()).commit();
                break;
            case R.id.action_emoticon:
                fragmentTransaction.replace(R.id.content, EmoticonsFragment.newInstance()).commit();
                break;
            case R.id.action_symbol:
                fragmentTransaction.replace(R.id.content, SymbolFragment.newInstance()).commit();
                break;
            case R.id.action_figlet:
                fragmentTransaction.replace(R.id.content, FigletFragment.newInstance()).commit();
                break;
            case R.id.action_more_app:
                StoreUtil.moreApp(MainActivity.this);
                return true;
            case R.id.action_rate:
                StoreUtil.gotoPlayStore(MainActivity.this, BuildConfig.APPLICATION_ID);
                return true;
            case R.id.action_share:
                StoreUtil.shareThisApp(MainActivity.this);
                return true;
            case R.id.action_text_converter:
                StoreUtil.gotoPlayStore(MainActivity.this, "duy.com.text_converter");
                return true;

        }
        return false;
    }
}
