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

package com.duy.ascii.art.emoticons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.R;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.favorite.localdata.DatabasePresenter;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.utils.ShareUtil;

import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

class EmoticonsAdapter extends RecyclerView.Adapter<EmoticonsAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    private final ArrayList<String> mItems = new ArrayList<>();
    protected LayoutInflater inflater;
    private Context context;
    private ClipboardManagerCompat clipboardManagerCompat;
    private DatabasePresenter mDatabasePresenter;


    public EmoticonsAdapter(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.mDatabasePresenter = new DatabasePresenter(context, null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_emoticon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String text = mItems.get(position);
        holder.txtContent.setText(text);
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManagerCompat.setText(holder.txtContent.getText().toString());
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.txtContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ShareUtil.shareText(holder.txtContent.getText().toString(), context);
                return false;
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
        return mItems.size();
    }

    public void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    public void add(String value) {
        this.mItems.add(value);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addAll(ArrayList<String> list) {
        this.mItems.clear();
        this.mItems.addAll(list);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public View root, imgFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
            root = itemView.findViewById(R.id.container);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
        }

    }
}

