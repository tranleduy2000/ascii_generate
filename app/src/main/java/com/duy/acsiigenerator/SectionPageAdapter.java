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

package com.duy.acsiigenerator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.duy.acsiigenerator.figlet.TextFragment;
import com.duy.acsiigenerator.image.ImageToAsciiFragment;

/**
 * Created by Duy on 15-Jun-17.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
    private TextFragment textFragment;
    private ImageToAsciiFragment imageToAsciiFragment;

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
        imageToAsciiFragment = ImageToAsciiFragment.newInstance();
        textFragment = TextFragment.newInstance();
    }

    public TextFragment getTextFragment() {
        return textFragment;
    }

    public ImageToAsciiFragment getImageToAsciiFragment() {
        return imageToAsciiFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return textFragment;
        } else if (position == 1) {
            return imageToAsciiFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "TEXT";
        } else if (position == 1) {
            return "IMAGE";
        }
        return super.getPageTitle(position);
    }
}
