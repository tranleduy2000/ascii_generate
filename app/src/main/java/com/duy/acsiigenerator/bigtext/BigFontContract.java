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

import java.util.ArrayList;

/**
 * Created by Duy on 07-Jul-17.
 */

public class BigFontContract {

    public interface View {
        void showResult(@NonNull ArrayList<String> result);

        void clearResult();

        void addResult(String text);

        void setPresenter(@Nullable Presenter presenter);

        void setProgress(int process);

        int getMaxProgress();

        void setColor(int color);

        void showProgress();

        void hideProgress();
    }

    public interface Presenter {
        void convert(String text);

        void cancel();
    }
}
