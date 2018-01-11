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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.emoji.model.EmojiCategory;
import com.duy.ascii.art.emoji.model.EmojiItem;
import com.duy.ascii.art.emoji.model.EmojiReader;
import com.duy.ascii.art.favorite.localdata.DatabasePresenter;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.utils.ShareUtil;
import com.duy.ascii.art.view.ViewPager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * Created by Duy on 9/27/2017.
 */

public class EmojiCategoriesFragment extends SimpleFragment implements OnPageChangeListener,
        View.OnClickListener {
    private static final String TAG = "CategoriesEmojiFragment";
    @Nullable
    private LoadDataTask mLoadDataTask;
    private ContentLoadingProgressBar mProgressBar;
    private EditText mEditInput;
    private DatabasePresenter mDatabasePresenter;

    public static EmojiCategoriesFragment newInstance() {

        Bundle args = new Bundle();

        EmojiCategoriesFragment fragment = new EmojiCategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_categories_emoji;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabasePresenter = new DatabasePresenter(getContext(), null);

        mEditInput = view.findViewById(R.id.edit_input);
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar);

        mLoadDataTask = new LoadDataTask(getContext());
        mLoadDataTask.execute();


        view.findViewById(R.id.btn_copy).setOnClickListener(this);
        view.findViewById(R.id.btn_share).setOnClickListener(this);
        view.findViewById(R.id.img_favorite).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_copy:
                ClipboardManagerCompat manager = ClipboardManagerCompatFactory.getManager(getContext());
                manager.setText(mEditInput.getText());
                Toast.makeText(getContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_share:
                ShareUtil.shareText(mEditInput.getText().toString(), getContext());
                break;
            case R.id.img_favorite:
                if (!mEditInput.getText().toString().isEmpty()) {
                    mDatabasePresenter.insert(new TextItem(mEditInput.getText().toString()));
                    Toast.makeText(getContext(), R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if (mLoadDataTask != null) mLoadDataTask.cancel(true);
        super.onDestroyView();
    }

    private void initView(ArrayList<EmojiCategory> data) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        EmojiClickListener listener = new EmojiClickListener() {
            @Override
            public void onClick(EmojiItem emojiItem) {
                String text = emojiItem.getEmojiChar();
                mEditInput.getText().insert(Math.max(0, mEditInput.getSelectionStart()), text);
            }
        };
        PagerSectionAdapter headerAdapter = new PagerSectionAdapter(getChildFragmentManager(), data, listener);
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


    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<EmojiCategory>> {
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
        protected ArrayList<EmojiCategory> doInBackground(Void... voids) {
            try {
                return EmojiReader.readData(getContext());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<EmojiCategory> pairs) {
            super.onPostExecute(pairs);
            mProgressBar.setVisibility(View.GONE);
            if (!isCancelled()) {
                initView(pairs);
            }
        }
    }


}
