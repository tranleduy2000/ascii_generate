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

package com.duy.ascii.sharedcode.emojiart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompat;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.sharedcode.emojiart.model.EmojiItem;
import com.duy.ascii.sharedcode.favorite.localdata.DatabasePresenter;
import com.duy.ascii.sharedcode.favorite.localdata.TextItem;
import com.duy.ascii.sharedcode.utils.ShareUtil;

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
    private ArrayList<EmojiItem> mEmojiItems = new ArrayList<>();
    private ClipboardManagerCompat mClipboard;
    private DatabasePresenter mDatabasePresenter;

    public RecentAdapter(Context context) {
        this.mContext = context;
        this.miInflater = LayoutInflater.from(context);
        this.mClipboard = ClipboardManagerCompatFactory.getManager(context);
        this.mDatabasePresenter = new DatabasePresenter(context, null);

    }

    public void addAll(List<EmojiItem> emojiItems) {
        addAll(mEmojiItems.size(), emojiItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = miInflater.inflate(R.layout.list_item_emoji_recent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String text = mEmojiItems.get(position).getContent();
        holder.txtContent.setText(text);
        holder.txtName.setText(mEmojiItems.get(position).getName());
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
    }

    @Override
    public int getItemCount() {
        return mEmojiItems.size();
    }

    public void add(EmojiItem value) {
        mEmojiItems.add(value);
        notifyItemInserted(mEmojiItems.size() - 1);
    }

    public EmojiItem getLastItem() {
        return mEmojiItems.get(mEmojiItems.size() - 1);
    }

    public EmojiItem getFirstItem() {
        return mEmojiItems.get(0);
    }

    public void addAll(int index, List<EmojiItem> emojiItems) {
        //sort decrease
        Collections.sort(emojiItems, new Comparator<EmojiItem>() {
            @Override
            public int compare(EmojiItem emojiItem, EmojiItem t1) {
                return -emojiItem.getTime().compareTo(t1.getTime());
            }
        });
        mEmojiItems.addAll(index, emojiItems);
        notifyItemRangeInserted(index, emojiItems.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtName;
        View imgCopy, imgShare, imgFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtName = itemView.findViewById(R.id.txt_name);
            imgCopy = itemView.findViewById(R.id.img_copy);
            imgShare = itemView.findViewById(R.id.img_share);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
        }
    }
}
