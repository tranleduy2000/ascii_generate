/*
 * Copyright (c) 2018 by Tran Le Duy
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.ascii.art.R;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.emoji.model.EmojiCategory;
import com.duy.ascii.art.emoji.model.EmojiItem;


/**
 * Created by Duy on 06-May-17.
 */

class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    protected LayoutInflater mInflater;
    private Context mContext;
    private ClipboardManagerCompat mClipboardManagerCompat;
    private EmojiCategory mCategory;
    @Nullable
    private EmojiClickListener mListener;

    EmojiAdapter(@NonNull Context context, EmojiCategory category) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mClipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.mCategory = category;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EmojiItem emojiItem = mCategory.get(position);
        holder.txtContent.setText(mCategory.get(position).getEmojiChar());
        holder.txtContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(holder.txtContent, emojiItem);
                }
                return true;
            }
        });
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(emojiItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public void setListener(@Nullable EmojiClickListener listener) {
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
        }

    }
}

