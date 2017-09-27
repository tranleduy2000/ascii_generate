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

package com.duy.ascii.sharedcode.emoji;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Pair;
import android.view.View;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.SimpleFragment;
import com.duy.ascii.sharedcode.view.ViewPager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Duy on 9/27/2017.
 */

public class CategoriesEmojiFragment extends SimpleFragment implements android.support.v4.view.ViewPager.OnPageChangeListener {
    private static final String TAG = "CategoriesEmojiFragment";
    @Nullable
    private LoadDataTask mLoadDataTask;
    private ContentLoadingProgressBar mProgressBar;

    public static CategoriesEmojiFragment newInstance() {

        Bundle args = new Bundle();

        CategoriesEmojiFragment fragment = new CategoriesEmojiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_categories_emoji;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);
        mLoadDataTask = new LoadDataTask(getContext());
        mLoadDataTask.execute();
    }

    @Override
    public void onDestroyView() {
        if (mLoadDataTask != null) mLoadDataTask.cancel(true);
        super.onDestroyView();
    }

    private void initView(ArrayList<Pair<String, ArrayList<String>>> data) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerSectionAdapter headerAdapter = new PagerSectionAdapter(getChildFragmentManager(), data);
        viewPager.setAdapter(headerAdapter);
        ((TabLayout) findViewById(R.id.tab_layout)).setupWithViewPager(viewPager);
        viewPager.setCurrentItem(getLastPosition());
        viewPager.addOnPageChangeListener(this);
    }

    private int getLastPosition() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        return pref.getInt(TAG + "last_page", 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.edit().putInt(TAG + "last_page", position).apply();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<Pair<String, ArrayList<String>>>> {
        private final Context context;

        private LoadDataTask(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Pair<String, ArrayList<String>>> doInBackground(Void... voids) {
            return parseData();
        }

        @Override
        protected void onPostExecute(ArrayList<Pair<String, ArrayList<String>>> pairs) {
            super.onPostExecute(pairs);
            mProgressBar.setVisibility(View.GONE);
            if (!isCancelled()) {
                initView(pairs);
            }
        }

        private ArrayList<Pair<String, ArrayList<String>>> parseData() {
            ArrayList<Pair<String, ArrayList<String>>> emojis = new ArrayList<>();
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(getContext().getAssets().open("emoji/emoji.xml"));
                Element root = document.getDocumentElement();
                NodeList items = root.getElementsByTagName("item");
                for (int i = 0; i < items.getLength(); i++) {

                    Element item = (Element) items.item(i);
                    String name = item.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                    String data = item.getElementsByTagName("data").item(0).getChildNodes().item(0).getNodeValue();
                    String[] split = data.split("\\n");
                    ArrayList<String> list = new ArrayList<>();
                    for (String s : split) {
                        s = s.trim();
                        if (!s.isEmpty() && s.contains(" ")) {
                            list.add(s.substring(0, s.indexOf(" ")));
                        }
                    }
                    emojis.add(new Pair<>(name, list));
                    if (isCancelled()) {
                        return emojis;
                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
            return emojis;
        }

    }


}
