package com.duy.acsiigenerator;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duy.acsiigenerator.core.FastConvert;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import imagetotext.duy.com.asciigenerator.BuildConfig;
import imagetotext.duy.com.asciigenerator.R;

public class MainActivity extends AppCompatActivity implements ResultAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private EditText editIn;
    private RecyclerView mRecyclerView;
    private FastConvert fastConvert;
    private ProgressBar mProgressBar;
    private Runnable process = new Runnable() {
        @Override
        public void run() {
            generateResult();
        }
    };
    private Handler handler = new Handler();
    private Dialog dialog;
    private ResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fastConvert = new FastConvert(getAssets());
        bindView();
    }

    private void bindView() {
        editIn = (EditText) findViewById(R.id.edit_in);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mRecyclerView = (RecyclerView) findViewById(R.id.listview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ResultAdapter(MainActivity.this, findViewById(R.id.empty_view));
        mAdapter.setOnItemClickListener(MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

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
        Log.d(TAG, "onResume() called");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editIn.setText(sharedPreferences.getString("key_save", ""));

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("key_save", editIn.getText().toString()).apply();
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
        }
        return false;
    }

    private void generateResult() {
        new TaskGenerateData().execute(editIn.getText().toString());
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

    @Override
    public void onSaveImage(final Bitmap bitmap) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save image");
        builder.setView(R.layout.dialog_save_file);
        this.dialog = builder.create();
        dialog.show();

        final String fileName = Integer.toHexString((int) System.currentTimeMillis()) + ".png";
        final EditText editName = (EditText) dialog.findViewById(R.id.edit_name);
        editName.setText(fileName);

        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty()) {
                    editName.setError(getString(R.string.enter_file_name));
                    return;
                }
                writeToStorage(bitmap, getFilesDir().getPath(), editName.getText().toString());
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.btn_save_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty()) {
                    editName.setError(getString(R.string.enter_file_name));
                    return;
                }

                String uri = writeToStorage(bitmap, getFilesDir().getPath(), editName.getText().toString());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "Share Image"));
                dialog.cancel();
            }
        });
    }

    private String writeToStorage(Bitmap bitmap, String path, String fileName) {
        File file = new File(path, fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            try {
                if (out != null) {
                    out.close();
                }
                return MediaStore.Images.Media.insertImage(getContentResolver(),
                        file.getAbsolutePath(), file.getName(), file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    private class TaskGenerateData extends AsyncTask<String, String, Void> {
        private static final float MAX_PROGRESS = 100;
        private AtomicInteger count = new AtomicInteger(0);
        private AtomicInteger current = new AtomicInteger(0);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setMax((int) MAX_PROGRESS);
            mProgressBar.setProgress(0);
            mAdapter.clear();
        }

        @Override
        protected Void doInBackground(String... params) {
            String input = params[0];
            try {
                if (input.isEmpty()) return null;
                AssetManager assets = getAssets();
                String[] files = assets.list("fonts");
                this.count.set(files.length);
                for (String fontName : files) {
                    try {
                        String s1 = fastConvert.convert(fontName, input);
                        publishProgress(s1);
                    } catch (Exception ignored) {
                    }
                }
            } catch (IOException ignored) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mAdapter.add(values[0]);
            mProgressBar.setProgress((int) (MAX_PROGRESS / count.get() * current.incrementAndGet()));
        }

        @Override
        protected void onPostExecute(Void strings) {
            super.onPostExecute(strings);
            Log.d(TAG, "onPostExecute() called with: strings = [" + strings + "]");

        }
    }

}
