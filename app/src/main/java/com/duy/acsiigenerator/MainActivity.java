package com.duy.acsiigenerator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.duy.acsiigenerator.figlet.ConvertContract;
import com.duy.acsiigenerator.figlet.ConvertPresenter;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.angads25.filepicker.view.FilePickerDialog;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import imagetotext.duy.com.asciigenerator.BuildConfig;
import imagetotext.duy.com.asciigenerator.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CoordinatorTabLayout mCoordinatorTabLayout;
    private ConvertPresenter mConvertPresenter;
    private ViewPager mViewPager;
    private SectionPageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindView();
        mConvertPresenter = new ConvertPresenter(getAssets(), mPageAdapter.getConvertFragment());
    }

    private void bindView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(mPageAdapter.getCount());

        int[] imageArray = new int[]{
                R.drawable.img1, R.drawable.img2};

        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.container);
        mCoordinatorTabLayout.setTitle("Demo")
                .setImageArray(imageArray)
                .setupWithViewPager(mViewPager);
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
                moreApp();
                return true;
            case R.id.action_rate:
                goToPlayStore();
                return true;
            case R.id.action_text_color:
                showDialogChooseColor();
                return true;
        }
        return false;
    }

    private void showDialogChooseColor() {
        AlertDialog build = ColorPickerDialogBuilder.with(this)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int i) {
                        ConvertContract.View view = mConvertPresenter.getView();
                        if (view != null) {
                            view.setColor(i);
                        }
                    }
                }).build();
        build.show();
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
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(MainActivity.this, R.string.permission_msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
