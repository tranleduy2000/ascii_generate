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

package com.duy.ascii.art.emoticons;

import com.duy.ascii.art.emoticons.model.EmoticonCategory;

import java.util.ArrayList;

/**
 * Created by Duy on 03-Jul-17.
 */

class EmoticonContract {
    public interface View {
        void showProgress();

        void hideProgress();

        void display(ArrayList<EmoticonCategory> list);

        void setPresenter(Presenter presenter);

    }

    public interface Presenter {
        void load(int index);

        void stop();
    }
}
