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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.emojiart.activities.CreateEmojiActivity;
import com.duy.ascii.art.emojiart.adapters.RecentAdapter;
import com.duy.ascii.art.emojiart.database.FirebaseHelper;
import com.duy.ascii.art.emojiart.model.EmojiItem;
import com.duy.ascii.art.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Duy on 9/27/2017.
 */

public class RecentFragment extends SimpleFragment {

    private static final String TAG = "RecentFragment";
    private static final int COUNT_PER_LOAD = 10;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView();
        loadAll();
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layout);
        mRecentAdapter = new RecentAdapter(getContext());
        mRecyclerView.setAdapter(mRecentAdapter);
        /*mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layout) {
            @Override
            public void onLoadMore(int current) {
                loadAll();
            }
        });*/
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
                loadAll();
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

    private void loadAll() {
        mSwipeRefreshLayout.setRefreshing(true);
        mDatabase.getAll(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addToRecyclerView(dataSnapshot, false, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void reload() {
        long time = 0;
        if (mRecentAdapter.getItemCount() > 0) {
            time = mRecentAdapter.getFirstItem().getTime() + 1;
        }
        mDatabase.recentFirst(time, 100, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                long childrenCount = dataSnapshot.getChildrenCount();
//                Log.d(TAG, "onDataChange: childrenCount = " + childrenCount);
                addToRecyclerView(dataSnapshot, false, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");
            }
        });
    }

    private void loadMoreItem() {
        mSwipeRefreshLayout.setRefreshing(true);
        long lastTime = System.currentTimeMillis();
        if (mRecentAdapter.getItemCount() > 0) {
            lastTime = mRecentAdapter.getLastItem().getTime() - 1;
//            Log.d(TAG, "showData: " + new Date(lastTime).toString());
        }
        mDatabase.recentLast(lastTime, COUNT_PER_LOAD, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                long childrenCount = dataSnapshot.getChildrenCount();
                addToRecyclerView(dataSnapshot, true, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");
            }
        });
    }


    private void addToRecyclerView(@Nullable DataSnapshot dataSnapshot, boolean addToLast, boolean sort) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 100);
        }
        if (dataSnapshot == null || dataSnapshot.getChildrenCount() == 0) {
            return;
        }
        Iterable<DataSnapshot> items = dataSnapshot.getChildren();
        ArrayList<EmojiItem> emojiItems = new ArrayList<>();
        for (DataSnapshot item : items) {
            try {
                EmojiItem value = item.getValue(EmojiItem.class);
                emojiItems.add(value);
            } catch (Exception ignored) {
            }
        }
        if (addToLast) {
            mRecentAdapter.addAll(emojiItems);
        } else {
            mRecentAdapter.addAll(0, emojiItems);
            if (mRecentAdapter.getItemCount() > 0) {
                mRecyclerView.scrollToPosition(0);
            }
        }
    }

    private void addTestData() {
        FirebaseHelper firebaseHelper = new FirebaseHelper(getContext());
        for (int i = 0; i < 3; i++) {
            firebaseHelper.add(new EmojiItem(0, System.currentTimeMillis(),
                    System.currentTimeMillis() + "", "duy", 0));
        }
    }
}
