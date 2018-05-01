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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.emoji.model.EmojiCategory;

/**
 * Created by Duy on 09-Aug-17.
 */

public class EmojiFragment extends SimpleFragment {
    public static final String TAG = "EmojiFragment";
    private static final String EXTRA_DATA = "data";
    private EmojiClickListener mListener;

    public static EmojiFragment newInstance(EmojiCategory emoji) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, emoji);
        EmojiFragment fragment = new EmojiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(EmojiClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_emoji;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EmojiCategory emojis = (EmojiCategory) getArguments().getSerializable(EXTRA_DATA);

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_emoji);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity(), emojis);
        recyclerView.setAdapter(emojiAdapter);

        emojiAdapter.setListener(mListener);


    }
}
