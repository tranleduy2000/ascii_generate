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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;

import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    protected LayoutInflater inflater;
    private Context context;
    private ClipboardManagerCompat clipboardManagerCompat;
    private ArrayList<Pair<String, String>> names;
    @Nullable
    private EmojiClickListener listener;


    public HeaderAdapter(@NonNull Context context, ArrayList<Pair<String, String>> names) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_header_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtPreview.setText(names.get(position).second);
        holder.txtName.setText(names.get(position).first);
    }

    public void setListener(EmojiClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtName;
        public TextView txtPreview;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPreview = itemView.findViewById(R.id.txt_preview);
            root = itemView.findViewById(R.id.container);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.container) {
                if (listener != null) {
                    listener.onClick(names.get(getAdapterPosition()).first);
                }
            }
        }
    }
}

