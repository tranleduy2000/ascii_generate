package com.duy.acsiigenerator.emoticons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.duy.acsiigenerator.emoticons.EmoticonContract;
import com.duy.acsiigenerator.emoticons.ShowAdapter;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 03-Jul-17.
 */

public class TextImageFragment extends BaseFragment implements EmoticonContract.View {
    public static final int INDEX = 2;

    @Override
    protected int getIndex() {
        return INDEX;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFacesAdapter = new ShowAdapter(getContext());
        mRecyclerView.setAdapter(mFacesAdapter);

        mProgressBar = view.findViewById(R.id.progress_bar);

    }
}