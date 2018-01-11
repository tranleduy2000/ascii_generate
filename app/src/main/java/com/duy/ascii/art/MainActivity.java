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

package com.duy.ascii.art;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.duy.ascii.art.asciiart.AsciiArtFragment;
import com.duy.ascii.art.bigtext.BigFontFragment;
import com.duy.ascii.art.emoji.CategoriesEmojiFragment;
import com.duy.ascii.art.emojiart.fragments.RecentFragment;
import com.duy.ascii.art.emoticons.EmoticonsFragment;
import com.duy.ascii.art.favorite.FavoriteActivity;
import com.duy.ascii.art.figlet.FigletFragment;
import com.duy.ascii.art.image.ImageToAsciiFragment;
import com.duy.ascii.art.purchase.AsciiPremium;
import com.duy.ascii.art.unicodesymbol.SymbolFragment;
import com.duy.common.purchase.InAppPurchaseActivity;
import com.duy.common.utils.StoreUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kobakei.ratethisapp.RateThisApp;


/**
 * Created by Duy on 09-Aug-17.
 */

public class MainActivity extends InAppPurchaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    @Nullable
    private AdView mAdView;
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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.content, AsciiArtFragment.newInstance())
                .commit();
        loadAdView();


        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }


    private void bindView() {
        mToolbar = findViewById(R.id.toolbar);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void loadAdView() {
        ViewGroup containerAd = mNavigationView.getHeaderView(0).findViewById(R.id.container_ad);
        View btnRemoveAd = mNavigationView.getHeaderView(0).findViewById(R.id.btn_remove_ads);
        if (AsciiPremium.isPremiumUser(this)) {
            btnRemoveAd.setVisibility(View.GONE);
            containerAd.setVisibility(View.GONE);
        } else {
            btnRemoveAd.setOnClickListener(this);
            btnRemoveAd.setVisibility(View.VISIBLE);
            containerAd.setVisibility(View.VISIBLE);
            mAdView = containerAd.findViewById(R.id.ad_view);
            AdRequest.Builder builder = new AdRequest.Builder();
            if (BuildConfig.DEBUG) builder.addTestDevice("D2281648CE409430157A9596175BF172");
            mAdView.loadAd(builder.build());
        }
    }

    @Override
    public void updateUi(boolean premium) {
        super.updateUi(premium);
        if (premium) {
            //invalidate
            loadAdView();
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
            case R.id.btn_remove_ads:
                showDialogUpgrade();
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_ascii_art:
                transaction.replace(R.id.content, AsciiArtFragment.newInstance()).commit();
                mToolbar.setSubtitle(R.string.ascii_art);
                break;
            case R.id.action_big_text:
                transaction.replace(R.id.content, BigFontFragment.newInstance()).commit();
                mToolbar.setSubtitle(R.string.big_text);
                break;
            case R.id.action_image_to_ascii:
                transaction.replace(R.id.content, ImageToAsciiFragment.newInstance()).commit();
                mToolbar.setSubtitle(R.string.image_to_ascii);
                break;
            case R.id.action_emoji:
                transaction.replace(R.id.content, CategoriesEmojiFragment.newInstance()).commit();
                mToolbar.setSubtitle(R.string.emoji);
                break;
            case R.id.action_emoji_art:
                mToolbar.setSubtitle(R.string.emoji_art);
                transaction.replace(R.id.content, RecentFragment.newInstance()).commit();
                break;
            case R.id.action_emoticon:
                transaction.replace(R.id.content, EmoticonsFragment.newInstance()).commit();
                mToolbar.setSubtitle(R.string.emoticons);
                break;
            case R.id.action_symbol:
                transaction.replace(R.id.content, SymbolFragment.newInstance()).commit();
                mToolbar.setSubtitle(R.string.cool_symbol);
                break;
            case R.id.action_figlet:
                mToolbar.setSubtitle(R.string.cool_symbol);
                transaction.replace(R.id.content, FigletFragment.newInstance()).commit();
                break;
            case R.id.action_rate:
                StoreUtil.gotoPlayStore(MainActivity.this, BuildConfig.APPLICATION_ID);
                return true;
            case R.id.action_share:
                StoreUtil.shareThisApp(MainActivity.this);
                return true;
            case R.id.action_more_app:
                StoreUtil.moreApp(MainActivity.this);
                return true;
        }
        return false;
    }
}
