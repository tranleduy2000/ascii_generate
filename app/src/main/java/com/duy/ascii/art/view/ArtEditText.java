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
