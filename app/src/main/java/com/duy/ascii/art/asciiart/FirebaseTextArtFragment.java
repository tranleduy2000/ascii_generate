/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.ascii.art.asciiart;

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

import com.duy.ascii.art.BuildConfig;
import com.duy.ascii.art.R;
import com.duy.ascii.art.SimpleFragment;
import com.duy.ascii.art.asciiart.database.FirebaseHelper;
import com.duy.ascii.art.asciiart.model.TextArt;
import com.duy.ascii.art.database.JavaSerialObject;
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

public class FirebaseTextArtFragment extends SimpleFragment {

    private static final String KEY_LAST_TIME = "pref_key_last_time_load_data";
    private static final String LOCAL_FILE = "emojiart_local";
    private RecyclerView mRecyclerView;
    private TextArtAdapter mTextArtAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseHelper mDatabase;
    private FloatingActionButton mFab;

    public static FirebaseTextArtFragment newInstance() {
        Bundle args = new Bundle();
        FirebaseTextArtFragment fragment = new FirebaseTextArtFragment();
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

    @SuppressWarnings("ConstantConditions")
    private void bindView() {
        mTextArtAdapter = new TextArtAdapter(getContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mTextArtAdapter);
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
                startActivity(new Intent(getContext(), CreateTextArtActivity.class));
            }
        });

        if (BuildConfig.DEBUG) {
            mTextArtAdapter.setListener(new TextArtAdapter.OnItemClickListener() {
                @Override
                public void onDelete(TextArt textArt) {
                    mDatabase.delete(textArt);
                }
            });
        }
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

    @SuppressWarnings({"unchecked", "ConstantConditions"})
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
        return pref.getLong(KEY_LAST_TIME, 0);
    }

    private void setLastTimeLoadData(long timeMillis) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.edit().putLong(KEY_LAST_TIME, timeMillis).apply();
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

    @SuppressWarnings("ConstantConditions")
    private void saveData(ArrayList<TextArt> textArts) {
        try {
            FileOutputStream stream = getContext().openFileOutput(LOCAL_FILE, Context.MODE_PRIVATE);
            JavaSerialObject.writeObject(textArts, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setData(ArrayList<TextArt> textArts) {
        mTextArtAdapter.clearAll();
        mTextArtAdapter.addAll(textArts);
        if (mTextArtAdapter.getItemCount() > 0) {
            mRecyclerView.scrollToPosition(0);
        }
    }

}
