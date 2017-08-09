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

package com.duy.ascii.sharedcode.view;

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
