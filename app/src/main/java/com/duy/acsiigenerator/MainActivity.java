package com.duy.acsiigenerator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.duy.acsiigenerator.emoticons.fragment.EmoticonFragment;
import com.duy.acsiigenerator.emoticons.fragment.TextImageFragment;
import com.duy.acsiigenerator.figlet.ConvertContract;
import com.duy.acsiigenerator.figlet.TextConvertPresenter;
import com.duy.acsiigenerator.figlet.TextFragment;
import com.duy.acsiigenerator.image.ImageToAsciiFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import imagetotext.duy.com.asciigenerator.BuildConfig;
import imagetotext.duy.com.asciigenerator.R;

public class MainActivity extends AppCompatActivity {

    public static final int EXTERNAL_READ_PERMISSION_GRANT = 1212;
    private static final String TAG = "MainActivity";
    private TextConvertPresenter mTextPresenter;

    private AdView mAdView;


    private Fragment mTextFragment;
    private Fragment mImageToAsciiFragment;
    private Fragment mEmoticonFragment;
    private Fragment mTextImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_main);

        mTextFragment = TextFragment.newInstance();
        mImageToAsciiFragment = ImageToAsciiFragment.newInstance();
        mEmoticonFragment = new EmoticonFragment();
        mTextImageFragment = new TextImageFragment();

        bindView();
        mTextPresenter = new TextConvertPresenter(getAssets(), (ConvertContract.View) mTextFragment);

        showAdView();
    }

    private void hideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showAdView() {
        mAdView = (AdView) findViewById(R.id.ad_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    private void bindView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_text: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, mTextFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.action_image: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, mImageToAsciiFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.action_emoticon: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, mEmoticonFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.action_text_image: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, mTextImageFragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_text);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_more_app:
                        moreApp();
                        return true;
                    case R.id.action_rate:
                        goToPlayStore();
                        return true;
                }
                return false;
            }
        });
    }

/*

    private void showDialogChooseColor() {
        AlertDialog build = ColorPickerDialogBuilder.with(this)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int i) {
                        ConvertContract.View view = mTextPresenter.getView();
                        if (view != null) {
                            view.setColor(i);
                        }
                    }
                }).build();
        build.show();
    }*/


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


}
