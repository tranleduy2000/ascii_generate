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

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;


/**
 * Created by Duy on 09-Aug-17.
 */

public class AdBannerActivity extends AppCompatActivity {
    private AdView mAdView;
    private NativeExpressAdView mNativeExpressAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAdViewIfNeed();
//        loadNativeAd();
    }

    protected void loadAdViewIfNeed() {
        try {
            mAdView = (AdView) findViewById(R.id.ad_view);
            if (mAdView != null) {
                if (BuildConfig.IS_PREMIUM_USER || hasPremiumApp()) {
                    mAdView.setVisibility(View.GONE);
                } else {
                    mAdView.loadAd(new AdRequest.Builder().build());
                }
            }
        } catch (Exception e) {
            //class cast
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) mAdView.pause();
        if (mNativeExpressAdView != null) mNativeExpressAdView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) mAdView.resume();
        if (mNativeExpressAdView != null) mNativeExpressAdView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) mAdView.destroy();
        if (mNativeExpressAdView != null) mNativeExpressAdView.destroy();
    }

    protected void loadNativeAd() {
        try {
            mNativeExpressAdView = (NativeExpressAdView) findViewById(R.id.native_ad_view);
            if (mNativeExpressAdView != null) {
                if (BuildConfig.IS_PREMIUM_USER) {
                    mNativeExpressAdView.setVisibility(View.GONE);
                } else {
                    mNativeExpressAdView.loadAd(new AdRequest.Builder().build());
                }
            }
        } catch (Exception e) {

        }
    }

    private boolean hasPremiumApp() {
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.duy.asciigenerator.pro", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
