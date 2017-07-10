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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.duy.ascii.sharedcode.StoreUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;

import imagetotext.duy.com.asciigenerator.BuildConfig;
import imagetotext.duy.com.asciigenerator.R;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private InterstitialAd interstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this);
        if (BuildConfig.DEBUG) {
            FirebaseCrash.setCrashCollectionEnabled(false);
        }
        hideStatusBar();
        setContentView(R.layout.activity_main);
        bindView();

        createAdInterstitial();
    }

    private void createAdInterstitial() {

        //create ad
        interstitialAd = new InterstitialAd(this);
        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        } else {
            interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        }
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
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

    private void hideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressWarnings("ConstantConditions")
    private void bindView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerSectionAdapter pagerSectionAdapter =
                new PagerSectionAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerSectionAdapter);
        viewPager.setOffscreenPageLimit(pagerSectionAdapter.getCount());
        viewPager.setCurrentItem(1); //big font
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_text_format_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_format_size_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_ads_white);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_texture_white_24dp);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_insert_emoticon_white_24dp);
        tabLayout.getTabAt(5).setIcon(R.drawable.ic_collections_white_24dp);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                return false;
            }
        });
    }


    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position) {
            case 2:
            case 3:
            case 4:
            case 5:
                hideKeyboard();
                break;
        }
    }

    private void hideKeyboard() {
        View currentFocus = getWindow().getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
