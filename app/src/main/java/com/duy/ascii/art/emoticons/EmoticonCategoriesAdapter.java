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

