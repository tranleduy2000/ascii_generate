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

package com.duy.ascii.sharedcode.bigtext;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duy on 07-Jul-17.
 */

public class BigFontPresenter implements BigFontContract.Presenter {
    private Reader cache = new Reader();
    private InputStream[] inputStreams;
    private BigFontContract.View view;
    private ProcessData process = new ProcessData();
    private Handler handler = new Handler();

    public BigFontPresenter(InputStream[] inputStream, BigFontContract.View view) {
        this.inputStreams = inputStream;
        this.view = view;
    }

    @Override
    public void convert(String text) {
        handler.removeCallbacks(process);
        process.setInput(text);
        handler.postDelayed(process, 300);
    }

    @Override
    public void cancel() {
        handler.removeCallbacks(process);
        process.cancel();
    }

    private class TaskGenerateData extends AsyncTask<String, String, Void> {
        private float maxProgress = 100;
        private AtomicInteger count = new AtomicInteger(0);
        private AtomicInteger current = new AtomicInteger(0);
        private BigFontContract.View view;

        public TaskGenerateData(BigFontContract.View view) {
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgress();
            maxProgress = view.getMaxProgress();
            view.setProgress(0);
            view.clearResult();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (!cache.isLoaded()) {
                cache.loadAndClose(inputStreams);
            }
            int size = cache.getSize();

            for (int i = 0; i < size && !isCancelled(); i++) {
                try {
                    String convert = cache.convert(params[0], i);
                    publishProgress(convert);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (isCancelled()) return;
            view.addResult(values[0]);
            view.setProgress((int) (maxProgress / count.get() * current.incrementAndGet()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.hideProgress();
        }
    }

    public class ProcessData implements Runnable {
        @Nullable
        TaskGenerateData taskGenerateData;
        private String input = "";

        public void setInput(String input) {
            this.input = input;
        }

        @Override
        public void run() {
            taskGenerateData = new TaskGenerateData(view);
            taskGenerateData.execute(input);
        }

        public void cancel() {
            if (taskGenerateData != null) {
                taskGenerateData.cancel(true);
            }
        }
    }
}
