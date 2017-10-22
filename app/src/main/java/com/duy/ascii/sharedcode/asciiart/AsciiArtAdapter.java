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

package com.duy.ascii.sharedcode.asciiart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompat;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.sharedcode.favorite.localdata.DatabasePresenter;
import com.duy.ascii.sharedcode.favorite.localdata.TextItem;
import com.duy.ascii.sharedcode.utils.ShareUtil;

import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

public class AsciiArtAdapter extends RecyclerView.Adapter<AsciiArtAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    private final ArrayList<String> objects = new ArrayList<>();
    protected LayoutInflater mInflater;
    private Context context;
    private ClipboardManagerCompat clipboardManagerCompat;
    private DatabasePresenter mDatabasePresenter;


    public AsciiArtAdapter(@NonNull Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.mDatabasePresenter = new DatabasePresenter(context, null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_ascii_art, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String text = objects.get(position);
        holder.txtContent.setText(text);
        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManagerCompat.setText(text);
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(text, context);
            }
        });
        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabasePresenter.insert(new TextItem(text));
                Toast.makeText(context, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void clear() {
        this.objects.clear();
        notifyDataSetChanged();
    }

    public void add(String value) {
        this.objects.add(value);
        notifyItemInserted(objects.size() - 1);
    }

    public void addAll(ArrayList<String> list) {
        this.objects.clear();
        this.objects.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public View root, imgFavorite, imgCopy, imgShare;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
            root = itemView.findViewById(R.id.container);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            imgCopy = itemView.findViewById(R.id.img_copy);
            imgShare = itemView.findViewById(R.id.img_share);
        }

    }


}

