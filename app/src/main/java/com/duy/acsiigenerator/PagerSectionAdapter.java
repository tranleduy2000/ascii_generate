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

import com.duy.acsiigenerator.emoticons.fragment.EmoticonFragment;
import com.duy.acsiigenerator.emoticons.fragment.TextImageFragment;
import com.duy.acsiigenerator.figlet.TextFragment;
import com.duy.acsiigenerator.image.ImageToAsciiFragment;

public class PagerSectionAdapter extends FragmentPagerAdapter {
    private static final int COUNT = 5;
    private String init;

    public PagerSectionAdapter(FragmentManager fm, String init) {
        super(fm);
        this.init = init;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TextImageFragment.newInstance();
            case 1:
                return AdsFragment.newInstance();
            case 2:
                return ImageToAsciiFragment.newInstance();
            case 3:
                return TextFragment.newInstance();
            case 4:
                return EmoticonFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Text Art";
            case 1:
                return "Ads";
            case 2:
                return "AsciiPhoto";
            case 3:
                return "Figlet";
            case 4:
                return "Emoticons";
            default:
                return "";
        }
    }

}
