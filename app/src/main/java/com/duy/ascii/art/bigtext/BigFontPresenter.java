/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.ascii.art.bigtext;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duy on 07-Jul-17.
 */

public class BigFontPresenter implements BigFontContract.Presenter {
    private BigFontGenerator cache = new BigFontGenerator();
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
                cache.load(inputStreams);
                for (InputStream inputStream : inputStreams) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
