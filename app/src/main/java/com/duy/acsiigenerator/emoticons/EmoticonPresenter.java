package com.duy.acsiigenerator.emoticons;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Duy on 03-Jul-17.
 */

public class EmoticonPresenter implements EmoticonContract.Presenter {
    private final Context context;
    private final EmoticonContract.View view;
    private AsyncTask<String, Void, ArrayList<String>> loadData;

    public EmoticonPresenter(Context context, EmoticonContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start(int index) {
        view.showProgress();
        if (loadData != null && !loadData.isCancelled()) {
            loadData.cancel(true);
        }
        loadData = new LoadDataTask(context, new LoadDataTask.Callback() {
            @Override
            public void onResult(ArrayList<String> list) {
                view.hideProgress();
                view.display(list);
            }
        });
        switch (index) {
            case EmoticonFragment.INDEX:
                loadData.execute("emoticons/faces.txt");
                break;
            case TextImageFragment.INDEX:
                loadData.execute("emoticons/image.txt");
                break;
        }
    }

    private static class LoadDataTask extends AsyncTask<String, Void, ArrayList<String>> {
        private Context context;
        private Callback callback;

        LoadDataTask(Context context, Callback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            AssetManager assets = context.getAssets();
            try {
                InputStream faces = assets.open(params[0]);
                return EmoticonManager.readFaces(faces);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);
            Log.d(TAG, "onPostExecute() called with: list = [" + list + "]");

            if (!isCancelled()) {
                callback.onResult(list);
            }
        }

        interface Callback {
            void onResult(ArrayList<String> list);
        }
    }
}
