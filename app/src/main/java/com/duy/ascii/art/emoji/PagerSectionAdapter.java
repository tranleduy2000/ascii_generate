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

    PagerSectionAdapter(FragmentManager fm, ArrayList<EmojiCategory> data, EmojiClickListener listener) {
        super(fm);
        this.mEmojis = data;
        this.mListener = listener;
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
