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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.duy.acsiigenerator.bigtext.BigFontFragment;
import com.duy.acsiigenerator.emoticons.fragment.EmoticonFragment;
import com.duy.acsiigenerator.emoticons.fragment.TextImageFragment;
import com.duy.acsiigenerator.figlet.FigletFragment;
import com.duy.acsiigenerator.fragments.AdsFragment;
import com.duy.acsiigenerator.image.ImageToAsciiFragment;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 07-Jul-17.
 */

public class PagerSectionAdapter extends FragmentPagerAdapter {
    private Context context;

    public PagerSectionAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FigletFragment.newInstance();
            case 1:
                return BigFontFragment.newInstance();
            case 2:
                return AdsFragment.newInstance();
            case 3:
                return TextImageFragment.newInstance();
            case 4:
                return EmoticonFragment.newInstance();
            case 5:
                return ImageToAsciiFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.figlet);
            case 1:
                return context.getString(R.string.big_text);
            case 2:
                return context.getString(R.string.ads);
            case 3:
                return context.getString(R.string.text_image);
            case 4:
                return context.getString(R.string.emoticon);
            case 5:
                return context.getString(R.string.photo);
            default:
                return null;
        }
    }
}
