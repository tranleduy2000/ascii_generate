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

package com.duy.ascii.art.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by Duy on 09-Jul-17.
 */

public class ArtEditText extends AppCompatEditText {
    private int row;
    private int col;

    public ArtEditText(Context context) {
        super(context);
    }

    public ArtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void create(int row, int col) {
        this.row = row;
        this.col = col;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                builder.append(Char.SPACE);
            }
            builder.append("\n");
        }
        setText(builder.toString());
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart == -1 || getText().length() == 0) return;

        if (selStart == getText().length()) selStart--;
        setSelection(selStart, selStart + 1);
    }

}
