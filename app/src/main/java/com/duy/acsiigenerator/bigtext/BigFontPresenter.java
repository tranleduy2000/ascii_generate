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

package com.duy.acsiigenerator.bigtext;

import android.os.AsyncTask;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duy on 07-Jul-17.
 */

public class BigFontPresenter implements BigFontContract.Presenter {
    private Reader cache = new Reader();
    private InputStream[] inputStreams;
    private BigFontContract.View view;

    public BigFontPresenter(InputStream[] inputStream, BigFontContract.View view) {
        this.inputStreams = inputStream;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void convert(String text) {
        if (!cache.isLoaded()) {
            cache.loadAndClose(inputStreams);
        }
        new TaskGenerateData(view, cache).execute(text);
    }

    private static class TaskGenerateData extends AsyncTask<String, String, Void> {
        private float maxProgress = 100;
        private AtomicInteger count = new AtomicInteger(0);
        private AtomicInteger current = new AtomicInteger(0);
        private BigFontContract.View view;
        private Reader reader;

        public TaskGenerateData(BigFontContract.View view, Reader reader) {
            this.view = view;
            this.reader = reader;
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
            int size = reader.getSize();
            for (int i = 0; i < size && !isCancelled(); i++) {
                try {
                    String convert = reader.convert(params[0], i);
                    view.addResult(convert);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            view.addResult(values[0]);
            view.setProgress((int) (maxProgress / count.get() * current.incrementAndGet()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.hideProgress();
        }
    }
}
