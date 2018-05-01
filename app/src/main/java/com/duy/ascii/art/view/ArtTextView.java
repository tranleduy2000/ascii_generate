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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Duy on 09-Jul-17.
 */

public class ArtTextView extends AppCompatTextView implements View.OnClickListener {
    private int pos = 0;

    public ArtTextView(Context context) {
        super(context);
        init(context);
    }

    public ArtTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArtTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        setOnClickListener(this);
        setText(Char.TEXTS[0]);

        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f,
                getResources().getDisplayMetrics());
        this.setTextSize(textSize);
    }

    @Override
    public void onClick(View v) {
        pos++;
        if (pos == Char.TEXTS.length) {
            pos = 0;
        }
        setText(Char.TEXTS[pos]);
    }
}
