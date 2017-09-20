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

package com.duy.ascii.sharedcode.favorite;

import android.content.Context;
import android.content.Intent;
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
import com.duy.ascii.sharedcode.favorite.localdata.DatabaseHelper;
import com.duy.ascii.sharedcode.favorite.localdata.TextItem;

import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private static final String TAG = "ResultAdapter";
    private final ArrayList<TextItem> mObjects = new ArrayList<>();
    protected LayoutInflater inflater;
    private Context context;
    private ClipboardManagerCompat mClipboard;
    private DatabaseHelper mDatabaseHelper;

    public FavoriteAdapter(@NonNull Context context, DatabaseHelper databaseHelper) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mClipboard = ClipboardManagerCompatFactory.getManager(context);
        this.mDatabaseHelper = databaseHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_favorite, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TextItem item = mObjects.get(position);
        final String text = item.getText();
        holder.txtContent.setText(text);
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClipboard.setText(holder.txtContent.getText().toString());
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.txtContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("text/plain");
                context.startActivity(intent);
                return false;
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(item, holder.getAdapterPosition());
            }
        });
    }

    private void remove(TextItem item, int adapterPosition) {
        mDatabaseHelper.delete(item);
        mObjects.remove(item);
        if (adapterPosition > -1) {
            notifyItemRemoved(adapterPosition);
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public void clear() {
        this.mObjects.clear();
        notifyDataSetChanged();
    }

    public void add(TextItem value) {
        this.mObjects.add(value);
        notifyItemInserted(mObjects.size() - 1);
    }

    public void addAll(ArrayList<TextItem> list) {
        this.mObjects.clear();
        this.mObjects.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public View root, imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
            root = itemView.findViewById(R.id.container);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }

    }
}

