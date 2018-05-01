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

package com.duy.ascii.art.emojiart.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.database.JavaSerialObject;
import com.duy.ascii.art.emojiart.activities.CreateEmojiActivity;
import com.duy.ascii.art.emojiart.adapters.RecentAdapter;
import com.duy.ascii.art.emojiart.database.FirebaseHelper;
import com.duy.ascii.art.emojiart.model.TextArt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.duy.ascii.art.database.JavaSerialObject.readObject;

/**
 * Created by Duy on 9/27/2017.
 */

public class RecentFragment extends SimpleFragment {

    private static final String TAG = "RecentFragment";
    private static final int COUNT_PER_LOAD = 10;
    private static final String EXTRA_LAST_TIME = "pref_key_last_time_load_data";
    private static final String LOCAL_FILE = "emojiart_local";
    private RecyclerView mRecyclerView;
    private RecentAdapter mRecentAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseHelper mDatabase;
    private FloatingActionButton mFab;

    public static RecentFragment newInstance() {

        Bundle args = new Bundle();

        RecentFragment fragment = new RecentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayout() {
        return R.layout.fragment_recent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new FirebaseHelper(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView();
        loadAll(false);
    }

    private void bindView() {
        mRecentAdapter = new RecentAdapter(getContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mRecentAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && mFab.isShown()) {
                    mFab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mFab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAll(true);
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.fab_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateEmojiActivity.class));
            }
        });
    }

    private void loadAll(boolean force) {
        mSwipeRefreshLayout.setRefreshing(true);
        if (showLoadNewData() || force) {
            loadFromFirebase();
        } else {
            loadFromLocal();
        }
    }

    private void loadFromFirebase() {
        Toast.makeText(getContext(), "Updating database", Toast.LENGTH_SHORT).show();
        mDatabase.getAll(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgress();
                setData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress();
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadFromLocal() {
        try {
            FileInputStream inputStream = getContext().openFileInput(LOCAL_FILE);
            ArrayList<TextArt> object = (ArrayList<TextArt>) readObject(inputStream);
            if (object == null) {
                loadFromFirebase();
                return;
            }
            setData(object);
            hideProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgress() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 100);
        }
    }

    private boolean showLoadNewData() {
        return System.currentTimeMillis() - getLastTimeLoadData() >= 3600000;
    }

    private long getLastTimeLoadData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        return pref.getLong(EXTRA_LAST_TIME, 0);
    }

    private void setLastTimeLoadData(long timeMillis) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.edit().putLong(EXTRA_LAST_TIME, timeMillis).apply();
    }


    private void setData(@Nullable DataSnapshot dataSnapshot) {
        if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0) {
            return;
        }
        ArrayList<TextArt> textArts = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            try {
                TextArt value = item.getValue(TextArt.class);
                textArts.add(value);
            } catch (Exception ignored) {
            }
        }
        setLastTimeLoadData(System.currentTimeMillis());
        saveData(textArts);
        setData(textArts);
    }

    private void saveData(ArrayList<TextArt> textArts) {
        try {
            FileOutputStream stream = getContext().openFileOutput(LOCAL_FILE, Context.MODE_PRIVATE);
            JavaSerialObject.writeObject(textArts, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setData(ArrayList<TextArt> textArts) {
        mRecentAdapter.clearAll();
        mRecentAdapter.addAll(textArts);
        if (mRecentAdapter.getItemCount() > 0) {
            mRecyclerView.scrollToPosition(0);
        }
    }

}
