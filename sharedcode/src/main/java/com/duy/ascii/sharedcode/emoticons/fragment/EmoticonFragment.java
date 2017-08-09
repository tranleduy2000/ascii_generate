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

package com.duy.ascii.sharedcode.emoticons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.emoticons.EmoticonContract;
import com.duy.ascii.sharedcode.emoticons.EmoticonsAdapter;


/**
 * Created by Duy on 03-Jul-17.
 */

public class EmoticonFragment extends BaseFragment implements EmoticonContract.View {
    public static final int INDEX = 1;

    public static EmoticonFragment newInstance() {

        Bundle args = new Bundle();

        EmoticonFragment fragment = new EmoticonFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getIndex() {
        return INDEX;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mFacesAdapter = new EmoticonsAdapter(getContext());
        mRecyclerView.setAdapter(mFacesAdapter);

        mProgressBar = view.findViewById(R.id.progress_bar);

    }

}
