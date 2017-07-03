package com.duy.acsiigenerator.emoticons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 03-Jul-17.
 */

public class TextImageFragment extends Fragment implements EmoticonContract.View {
    public static final int INDEX = 2;
    private EmoticonContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private FacesAdapter mFacesAdapter;
    private ContentLoadingProgressBar mProgressBar;

    @Override
    public void showProgress() {
        mProgressBar.show();
    }

    @Override
    public void hideProgress() {
        mProgressBar.hide();
    }

    @Override
    public void display(ArrayList<String> list) {
        mFacesAdapter.clear();
        mFacesAdapter.addAll(list);
    }

    @Override
    public void setPresenter(EmoticonContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void append(String value) {
        mFacesAdapter.add(value);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mPresenter = new EmoticonPresenter(getContext(), this);
        return inflater.inflate(R.layout.fragment_emoticon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFacesAdapter = new FacesAdapter(getContext());
        mRecyclerView.setAdapter(mFacesAdapter);

        mProgressBar = view.findViewById(R.id.progress_bar);

        mPresenter.start(INDEX);
    }
}