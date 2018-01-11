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

package com.duy.ascii.art.emoticons;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.emoticons.model.EmoticonCategory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import it.sephiroth.android.library.tooltip.Tooltip;

import static com.duy.ascii.art.utils.FileUtil.streamToString;

/**
 * Created by Duy on 9/27/2017.
 */

public class EmoticonsFragment extends SimpleFragment implements EmoticonContract.View, EmoticonCategoriesAdapter.OnCategoryClickListener {
    protected EmoticonContract.Presenter mPresenter;
    protected RecyclerView mCategoriesView, mContentView;
    protected EmoticonCategoriesAdapter mCategoriesAdapter;
    protected EmoticonsAdapter mContentAdapter;
    protected ContentLoadingProgressBar mProgressBar;
    private LoadDataTask mLoadDataTask;
    private Toolbar mToolbar;

    public static EmoticonsFragment newInstance() {

        Bundle args = new Bundle();

        EmoticonsFragment fragment = new EmoticonsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_emoticons;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = getActivity().findViewById(R.id.toolbar);
        mCategoriesView = (RecyclerView) findViewById(R.id.recycle_view_header);
        mCategoriesView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCategoriesAdapter = new EmoticonCategoriesAdapter(getContext());
        mCategoriesView.setAdapter(mCategoriesAdapter);
        mCategoriesView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mCategoriesAdapter.setOnCategoryClickListener(this);

        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.hide();

        mContentView = (RecyclerView) findViewById(R.id.recycle_view_content);
        mContentView.setHasFixedSize(true);
        mContentView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mContentAdapter = new EmoticonsAdapter(getContext());
        mContentView.setAdapter(mContentAdapter);

        mLoadDataTask = new LoadDataTask(getContext(), this);
        mLoadDataTask.execute();
    }

    @Override
    public void showProgress() {
        mProgressBar.show();
    }

    @Override
    public void hideProgress() {
        mProgressBar.hide();
    }

    @Override
    public void display(ArrayList<EmoticonCategory> list) {
        mCategoriesAdapter.setData(list);
    }

    @Override
    public void setPresenter(EmoticonContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onHeaderClick(EmoticonCategory category) {
        mToolbar.setSubtitle(category.getTitle());
        mContentAdapter.clear();
        mContentAdapter.addAll(category.getData());
    }

    @Override
    public void onHeaderLongClick(View view, EmoticonCategory category) {
        bottomToolTipDialogBox(view, category.getDescription());
    }

    public void bottomToolTipDialogBox(View view, String description) {
        Tooltip.Builder builder = new Tooltip.Builder(101)
                .anchor(view, Tooltip.Gravity.BOTTOM)
                .closePolicy(new Tooltip.ClosePolicy()
                        .insidePolicy(true, false)
                        .outsidePolicy(true, false), 4000)
                .activateDelay(900)
                .showDelay(400)
                .text(description)
                .maxWidth(600)
                .withArrow(true)
                .withOverlay(true);
        Tooltip.make(getContext(), builder.build()).show();
    }


    @Override
    public void onDestroyView() {
        if (mLoadDataTask != null) mLoadDataTask.cancel(true);
        super.onDestroyView();
    }

    private static class LoadDataTask extends AsyncTask<Void, Void, ArrayList<EmoticonCategory>> {
        private Context context;
        private EmoticonContract.View view;

        LoadDataTask(Context context, EmoticonContract.View view) {
            this.context = context;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgress();
        }

        @Override
        protected ArrayList<EmoticonCategory> doInBackground(Void... params) {
            AssetManager assets = context.getAssets();
            ArrayList<EmoticonCategory> categories = new ArrayList<>();
            try {
                String[] files = assets.list("emoticons");
                for (String fileName : files) {
                    if (isCancelled()) break;
                    InputStream stream = assets.open("emoticons/" + fileName);
                    JSONObject object = new JSONObject(streamToString(stream));
                    JSONArray jsonArray = object.getJSONArray("data");
                    ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        data.add(jsonArray.getString(i));
                    }
                    EmoticonCategory item = new EmoticonCategory(
                            object.getString(EmoticonCategory.TITLE),
                            object.getString(EmoticonCategory.DESCRIPTION),
                            data);
                    categories.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categories;
        }


        @Override
        protected void onPostExecute(ArrayList<EmoticonCategory> list) {
            super.onPostExecute(list);
            if (isCancelled()) return;
            view.hideProgress();
            view.display(list);
        }
    }
}
