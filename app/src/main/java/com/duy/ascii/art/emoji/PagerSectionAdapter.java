/*
 * Copyright (c) 2018 by Tran Le Duy
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

package com.duy.ascii.art.emoji;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.duy.ascii.art.emoji.model.EmojiCategory;

import java.util.ArrayList;

/**
 * Created by Duy on 9/27/2017.
 */
class PagerSectionAdapter extends FragmentPagerAdapter {
    private ArrayList<EmojiCategory> mEmojis = new ArrayList<>();
    private EmojiClickListener mListener;

    PagerSectionAdapter(FragmentManager fm, ArrayList<EmojiCategory> data,
                        EmojiClickListener mListener) {
        super(fm);
        this.mEmojis = data;
        this.mListener = mListener;
    }

    @Override
    public Fragment getItem(int position) {
        EmojiFragment emojiFragment = EmojiFragment.newInstance(mEmojis.get(position));
        emojiFragment.setListener(mListener);
        return emojiFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mEmojis.get(position).getName();
    }

    @Override
    public int getCount() {
        return mEmojis.size();
    }
}
