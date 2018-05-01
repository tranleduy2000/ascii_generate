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
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * Created by Duy on 09-Jul-17.
 */

public class ArtViewGroup extends RelativeLayout {
    private int row;
    private int col;

    public ArtViewGroup(Context context) {
        super(context);
    }

    public ArtViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArtViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void create(int row, int col) {
        removeAllViews();

        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics());


        this.row = row;
        this.col = col;
        @IdRes int id = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ArtTextView artTextView = new ArtTextView(getContext());
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                artTextView.setId(id);
                artTextView.setText(Char.SPACE);
                if (j == 0) {
                    params.addRule(ALIGN_PARENT_LEFT, TRUE);
                } else {
                    params.addRule(RIGHT_OF, id - 1);
                }
                if (i == 0) {
                    params.addRule(ALIGN_PARENT_TOP, TRUE);
                } else {
                    params.addRule(BELOW, id - col);
                }
                params.setMargins(margin, 0, margin, 0);
                artTextView.setLayoutParams(params);
                addView(artTextView);
                id++;
            }
        }

    }
}
