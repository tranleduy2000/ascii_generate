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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Duy on 07-Jul-17.
 */
public class BigFontPresenterTest {
    @Test
    public void convert() throws Exception {
        File file = new File("C:\\github\\AsciiGenerator\\app\\src\\main\\java\\com\\duy\\acsiigenerator\\bigtext");
        File[] files = file.listFiles();
        FileInputStream[] inputStreams = new FileInputStream[files.length];
        for (int i = 0; i < files.length; i++) {
            inputStreams[i] = new FileInputStream(files[i]);
        }
        BigFontPresenter bigFontPresenter = new BigFontPresenter(inputStreams, new BigFontContract.View() {
            @Override
            public void showResult(@NonNull ArrayList<String> result) {

            }

            @Override
            public void clearResult() {

            }

            @Override
            public void addResult(String text) {
                System.out.println(text);
            }

            @Override
            public void setPresenter(@Nullable BigFontContract.Presenter presenter) {

            }

            @Override
            public void setProgress(int process) {

            }

            @Override
            public int getMaxProgress() {
                return 0;
            }

            @Override
            public void setColor(int color) {

            }

            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        });
        bigFontPresenter.convert("HELLO EVERYONE");
    }

}