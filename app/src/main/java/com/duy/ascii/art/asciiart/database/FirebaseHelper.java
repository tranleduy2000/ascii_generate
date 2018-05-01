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

package com.duy.ascii.art.asciiart.database;

import android.content.Context;
import android.util.Log;

import com.duy.ascii.art.asciiart.model.TextArt;
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

    public void add(TextArt textArt) {
        Log.d(TAG, "add() called with: emojiItem = [" + textArt + "]");
        DatabaseReference reference = mFirebaseDatabase.getReference();
        DatabaseReference recent = reference.child(Constants.ROOT);
        recent.push().setValue(textArt);
    }

    public ArrayList<TextArt> getPopular() {
        return null;
    }

    public void delete(TextArt textArt) {
        DatabaseReference ref = mFirebaseDatabase.getReference();
        final DatabaseReference database = ref.child(Constants.ROOT);
        Query query = database
                .orderByChild("time")
                .equalTo(textArt.getTime());
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
