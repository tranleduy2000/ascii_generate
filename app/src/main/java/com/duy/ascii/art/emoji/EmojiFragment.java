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

package com.duy.ascii.art.emoji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.art.SimpleFragment;

import java.util.ArrayList;

/**
 * Created by Duy on 09-Aug-17.
 */

public class EmojiFragment extends SimpleFragment {
    public static final String TAG = "EmojiFragment";
    private EmojiClickListener mListener;

    public static EmojiFragment newInstance(ArrayList<String> emoji) {

        Bundle args = new Bundle();
        args.putSerializable("data", emoji);
        EmojiFragment fragment = new EmojiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(EmojiClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_emoji;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> emojis = (ArrayList<String>) getArguments().getSerializable("data");

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_emoji);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity(), emojis);
        recyclerView.setAdapter(emojiAdapter);

        emojiAdapter.setListener(mListener);


    }
}
