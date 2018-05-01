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

import com.duy.ascii.art.R;
import com.duy.ascii.art.emoticons.model.EmoticonCategory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Duy on 06-May-17.
 */

class EmoticonCategoriesAdapter extends RecyclerView.Adapter<EmoticonCategoriesAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    private static final String PATH = "emoticons";
    private final ArrayList<EmoticonCategory> mCategories = new ArrayList<>();
    protected LayoutInflater mInflater;
    private OnCategoryClickListener mOnCategoryClickListener;


    public EmoticonCategoriesAdapter(@NonNull Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<EmoticonCategory> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_header_emoticons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final EmoticonCategory category = mCategories.get(position);
        holder.txtContent.setText(category.getTitle());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCategoryClickListener != null) {
                    mOnCategoryClickListener.onHeaderClick(category);
                }
            }
        });
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnCategoryClickListener != null) {
                    mOnCategoryClickListener.onHeaderLongClick(holder.root, category);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.mOnCategoryClickListener = onCategoryClickListener;
    }

    public interface OnCategoryClickListener {
        void onHeaderClick(EmoticonCategory category);

        void onHeaderLongClick(View view, EmoticonCategory category);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public View root;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txt_name);
            root = itemView.findViewById(R.id.container);
        }

    }
}

