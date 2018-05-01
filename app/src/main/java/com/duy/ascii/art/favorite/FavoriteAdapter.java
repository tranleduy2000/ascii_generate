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

package com.duy.ascii.art.favorite;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.favorite.localdata.DatabaseHelper;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.R;

import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private static final String TAG = "ResultAdapter";
    private final ArrayList<TextItem> mObjects = new ArrayList<>();
    protected LayoutInflater inflater;
    private Context context;
    private ClipboardManagerCompat mClipboard;
    private DatabaseHelper mDatabaseHelper;

    FavoriteAdapter(@NonNull Context context, DatabaseHelper databaseHelper) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        View root, imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
            imgDelete = itemView.findViewById(R.id.img_delete);
            root = itemView.findViewById(R.id.container);
        }

    }
}

