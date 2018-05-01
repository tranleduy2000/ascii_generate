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

package com.duy.ascii.art.emojiart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.BuildConfig;
import com.duy.ascii.art.R;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.emojiart.database.FirebaseHelper;
import com.duy.ascii.art.emojiart.model.TextArt;
import com.duy.ascii.art.favorite.localdata.DatabasePresenter;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.utils.ShareUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Duy on 9/27/2017.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {
    private static final String TAG = "RecentAdapter";
    private final Context mContext;
    private LayoutInflater miInflater;
    private ArrayList<TextArt> mTextArts = new ArrayList<>();
    private ClipboardManagerCompat mClipboard;
    private DatabasePresenter mDatabasePresenter;
    private FirebaseHelper mFirebaseHelper;

    public RecentAdapter(Context context) {
        this.mContext = context;
        this.miInflater = LayoutInflater.from(context);
        this.mClipboard = ClipboardManagerCompatFactory.getManager(context);
        this.mDatabasePresenter = new DatabasePresenter(context, null);
        this.mFirebaseHelper = new FirebaseHelper(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = miInflater.inflate(R.layout.list_item_emoji_recent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String text = mTextArts.get(position).getContent();
        final TextArt textArt = mTextArts.get(position);

        holder.txtContent.setText(text);
        holder.txtName.setText(mTextArts.get(position).getName());
        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipboard.setText(holder.txtContent.getText().toString());
                Toast.makeText(mContext, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(holder.txtContent.getText().toString(), mContext);
            }
        });
        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabasePresenter.insert(new TextItem(text));
                Toast.makeText(mContext, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
            }
        });

        if (BuildConfig.DEBUG) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(textArt);
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDelete.setOnClickListener(null);
        }
    }

    private void delete(TextArt textArt) {
        mFirebaseHelper.delete(textArt);
        int index = mTextArts.indexOf(textArt);
        if (index >= 0) {
            mTextArts.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public int getItemCount() {
        return mTextArts.size();
    }

    public void add(TextArt value) {
        mTextArts.add(value);
        notifyItemInserted(mTextArts.size() - 1);
    }

    public TextArt getLastItem() {
        return mTextArts.get(mTextArts.size() - 1);
    }

    public TextArt getFirstItem() {
        return mTextArts.get(0);
    }

    public void addAll(List<TextArt> textArts) {
        //sort decrease
        Collections.sort(textArts, new Comparator<TextArt>() {
            @Override
            public int compare(TextArt textArt, TextArt t1) {
                return -textArt.getTime().compareTo(t1.getTime());
            }
        });
        mTextArts.addAll(textArts);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mTextArts.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtName;
        View imgCopy, imgShare, imgFavorite, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtName = itemView.findViewById(R.id.txt_name);
            imgCopy = itemView.findViewById(R.id.img_copy);
            imgShare = itemView.findViewById(R.id.img_share);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
