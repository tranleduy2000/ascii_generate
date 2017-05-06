package com.duy.acsiigenerator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.duy.acsiigenerator.core.FastConvert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import imagetotext.duy.com.asciigenerator.BuildConfig;
import imagetotext.duy.com.asciigenerator.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editIn;
    private RecyclerView recyclerView;
    private FastConvert fastConvert;
    private ProgressBar progressBar;
    private Runnable process = new Runnable() {
        @Override
        public void run() {
            generateResult();
        }
    };
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPlayStore();
            }
        });

        fastConvert = new FastConvert(getAssets());
        editIn = (EditText) findViewById(R.id.edit_in);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.listview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(process);
                handler.postDelayed(process, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editIn.setText(sharedPreferences.getString("key_save", ""));

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("key_save", editIn.getText().toString()).apply();
    }

    private void generateResult() {
        new TaskGenerateData().execute(editIn.getText().toString());
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

    private class TaskGenerateData extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setIndeterminate(true);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String input = params[0];
            ArrayList<String> result = new ArrayList<>();
            try {

                if (input.isEmpty()) return result;
                AssetManager assets = getAssets();
                String[] files = assets.list("fonts");
                Log.d(TAG, "generateResult: " + Arrays.toString(files));
                for (String fontName : files) {
                    try {
                        String s1 = fastConvert.convert(fontName, input);
                        //                    for (int i = s1.length() - 1; i >= 0; i--) {
//                        if (copy.charAt(i) == ' ') {
//                            copy.insert(i, ' ');
//                        }
//                    }
                        result.add(s1);
                    } catch (Exception ignored) {
                    }
                }
            } catch (IOException ignored) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            ResultAdapter adapter = new ResultAdapter(MainActivity.this, strings, findViewById(R.id.empty_view));
            recyclerView.setAdapter(adapter);
            progressBar.setIndeterminate(false);
        }
    }

}
