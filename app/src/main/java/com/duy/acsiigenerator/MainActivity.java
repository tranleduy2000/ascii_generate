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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.duy.acsiigenerator.figlet.TextConvertPresenter;

import imagetotext.duy.com.asciigenerator.BuildConfig;
import imagetotext.duy.com.asciigenerator.R;

public class MainActivity extends AppCompatActivity {

    public static final int EXTERNAL_READ_PERMISSION_GRANT = 1212;
    private static final String TAG = "MainActivity";
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    private KeyBoardEventListener keyBoardListener;
    private ViewPager viewPager;
    private PagerSectionAdapter adapter;
    private TextConvertPresenter mTextPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main);

        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String text = null;
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.equals("text/plain")) {
                text = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new PagerSectionAdapter(getSupportFragmentManager(), text);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == AdsFragment.INDEX) {
                    hideKeyboard();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        //attach listener hide/show keyboard
        keyBoardListener = new KeyBoardEventListener(this);
        coordinatorLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyBoardListener);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_more:
                moreApp();
                break;
            case R.id.action_rate:
                goToPlayStore();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * hide appbar layout when keyboard visible
     */
    private void hideAppBar() {
        toolbar.setVisibility(View.GONE);
    }

    /**
     * show appbar layout when keyboard gone
     */
    private void showAppBar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    protected void onShowKeyboard() {
        hideAppBar();
    }

    protected void onHideKeyboard() {
        showAppBar();
    }

    private void hideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void moreApp() {
        Uri uri = Uri.parse("market://search?q=pub:Trần Lê Duy");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=pub:Trần Lê Duy")));
        }

    }

    public void goToPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |

                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(MainActivity.this, R.string.permission_msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class KeyBoardEventListener implements ViewTreeObserver.OnGlobalLayoutListener {
        MainActivity activity;

        KeyBoardEventListener(MainActivity activityIde) {
            this.activity = activityIde;
        }

        public void onGlobalLayout() {
            int i = 0;
            int navHeight = this.activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            navHeight = navHeight > 0 ? this.activity.getResources().getDimensionPixelSize(navHeight) : 0;
            int statusBarHeight = this.activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (statusBarHeight > 0) {
                i = this.activity.getResources().getDimensionPixelSize(statusBarHeight);
            }
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (activity.coordinatorLayout.getRootView().getHeight() - ((navHeight + i) + rect.height()) <= 0) {
                activity.onHideKeyboard();
            } else {
                activity.onShowKeyboard();
            }
        }
    }


}
