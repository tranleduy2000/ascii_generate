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

package com.duy.ascii.sharedcode.emoticons;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.SimpleFragment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Duy on 9/27/2017.
 */

public class EmoticonsFragment extends SimpleFragment implements EmoticonContract.View, HeaderAdapter.HeaderClickListener {
    protected EmoticonContract.Presenter mPresenter;
    protected RecyclerView mHeader, mContent;
    protected HeaderAdapter mAdapter;
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
        mHeader = (RecyclerView) findViewById(R.id.recycle_view_header);
        mHeader.setHasFixedSize(true);
        mHeader.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HeaderAdapter(getContext());
        mHeader.setAdapter(mAdapter);
        mHeader.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setListener(this);

        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.hide();

        mContent = (RecyclerView) findViewById(R.id.recycle_view_content);
        mContent.setHasFixedSize(true);
        mContent.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mContentAdapter = new EmoticonsAdapter(getContext());
        mContent.setAdapter(mContentAdapter);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String lastPath = pref.getString("last_path", "");
        if (!lastPath.isEmpty()) {
            onHeaderClick(lastPath);
        }
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
    public void display(ArrayList<String> list) {

    }

    @Override
    public void setPresenter(EmoticonContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void append(String value) {
        mContentAdapter.add(value);
    }

    @Override
    public void onHeaderClick(String path) {
        mToolbar.setSubtitle(HeaderAdapter.refine(path.substring(path.lastIndexOf("/") + 1)));
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.edit().putString("last_path", path).apply();
        if (mLoadDataTask != null && !mLoadDataTask.isCancelled()) {
            mLoadDataTask.cancel(true);
        }
        mContentAdapter.clear();
        mLoadDataTask = new LoadDataTask(getContext(), this);
        mLoadDataTask.execute(path);
    }

    @Override
    public void onDestroyView() {
        if (mLoadDataTask != null) mLoadDataTask.cancel(true);
        super.onDestroyView();
    }

    private static class LoadDataTask extends AsyncTask<String, String, Void> {
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
        protected Void doInBackground(String... params) {
            AssetManager assets = context.getAssets();
            try {
                InputStream stream = assets.open(params[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(stream);
                Element root = doc.getDocumentElement();
                NodeList name = root.getElementsByTagName("name");
                NodeList data = root.getElementsByTagName("data");
                for (int i = 0; i < data.getLength(); i++) {
                    Element list = (Element) data.item(i);
                    NodeList items = list.getElementsByTagName("item");
                    for (int j = 0; j < items.getLength(); j++) {
                        if (isCancelled()) return null;
                        Node item = items.item(j);
                        String string = item.getChildNodes().item(0).getTextContent();
                        publishProgress(string);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (!isCancelled()) {
                view.append(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void list) {
            super.onPostExecute(list);
            view.hideProgress();
        }
    }
}
