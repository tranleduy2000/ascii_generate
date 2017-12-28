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

package com.duy.ascii.art.emojiart.database;

import android.content.Context;
import android.util.Log;

import com.duy.ascii.art.emojiart.model.EmojiItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Duy on 9/27/2017.
 */

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private Context mContext;
    private FirebaseDatabase mFirebaseDatabase;


    public FirebaseHelper(Context context) {
        this.mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void recentLast(long lastTime, int size, ValueEventListener callback) {
        DatabaseReference reference = mFirebaseDatabase.getReference();
        DatabaseReference recent = reference.child(Constants.ROOT);
        Query query = recent
                .orderByChild("time")
                .endAt(lastTime) //query item which time <= lastTime
                .limitToLast(size); //select top
        query.addListenerForSingleValueEvent(callback);
    }


    public void getAll(final ValueEventListener callback) {
        DatabaseReference reference = mFirebaseDatabase.getReference();
        DatabaseReference all = reference.child(Constants.ROOT);
        all.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void popularLast(long lastTime, int size, ValueEventListener callback) {
        DatabaseReference reference = mFirebaseDatabase.getReference();
        DatabaseReference recent = reference.child(Constants.ROOT);
        Query query = recent
                .orderByChild("time")
                .endAt(lastTime) //query item which time <= lastTime
                .limitToLast(size); //select top
        query.addListenerForSingleValueEvent(callback);
    }

    public void recentFirst(long time, int size, ValueEventListener callback) {
        DatabaseReference reference = mFirebaseDatabase.getReference();
        DatabaseReference recent = reference.child(Constants.ROOT);
        Query query = recent
                .orderByChild("time")
                .startAt(time) //query item which time <= lastTime
                .limitToLast(size); //select bottom
        query.addListenerForSingleValueEvent(callback);
    }

    public void add(EmojiItem emojiItem) {
        Log.d(TAG, "add() called with: emojiItem = [" + emojiItem + "]");
        DatabaseReference reference = mFirebaseDatabase.getReference();
        DatabaseReference recent = reference.child(Constants.ROOT);
        recent.push().setValue(emojiItem);
    }

    public ArrayList<EmojiItem> getPopular() {
        return null;
    }

    public void delete(EmojiItem emojiItem) {
        DatabaseReference ref = mFirebaseDatabase.getReference();
        final DatabaseReference database = ref.child(Constants.ROOT);
        Query query = database
                .orderByChild("time")
                .equalTo(emojiItem.getTime());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
