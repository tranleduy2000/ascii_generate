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
