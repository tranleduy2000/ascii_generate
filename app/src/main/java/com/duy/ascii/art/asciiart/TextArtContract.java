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

package com.duy.ascii.art.asciiart;

import com.duy.ascii.art.asciiart.model.TextArt;

import java.util.ArrayList;

/**
 * Created by Duy on 03-Jul-17.
 */
class TextArtContract {
    public interface View {
        void showProgress();

        void hideProgress();

        void display(ArrayList<TextArt> list);

        void setPresenter(Presenter presenter);

        void append(TextArt value);
    }

    public interface Presenter {
        void onStart();

        void onStop();
    }
}
