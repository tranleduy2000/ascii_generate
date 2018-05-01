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
