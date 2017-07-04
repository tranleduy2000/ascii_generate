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

package com.duy.acsiigenerator.figlet.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.acsiigenerator.ImageFactory;
import com.duy.acsiigenerator.clipboard.ClipboardManagerCompat;
import com.duy.acsiigenerator.clipboard.ClipboardManagerCompatFactory;

import java.util.ArrayList;
import java.util.List;

import imagetotext.duy.com.asciigenerator.R;

/**
 * Created by Duy on 06-May-17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    private final List<String> objects = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private ClipboardManagerCompat clipboardManagerCompat;
    @Nullable
    private View emptyView;
    @Nullable
    private OnItemClickListener onItemClickListener;
    private int color;

    public ResultAdapter(@NonNull Context context, @Nullable View emptyView) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.emptyView = emptyView;
        invalidateEmptyView();
        this.color = context.getResources().getColor(android.R.color.primary_text_dark);
    }

    private void invalidateEmptyView() {
        if (objects.size() > 0) {
            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }
        } else {
            if (emptyView != null && emptyView.getVisibility() == View.GONE) {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtContent.setTypeface(Typeface.MONOSPACE);
        holder.txtContent.setText(objects.get(position));
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, holder.txtContent.getText().toString());
                intent.setType("text/plain");
                context.startActivity(intent);
            }
        });

        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManagerCompat.setText(holder.txtContent.getText().toString());
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onSaveImage(ImageFactory.createImageFromView(holder.txtContent));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void clear() {
        this.objects.clear();
        notifyDataSetChanged();
        invalidateEmptyView();
    }

    public void add(String value) {
        this.objects.add(value);
        notifyItemInserted(objects.size() - 1);
        invalidateEmptyView();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public interface OnItemClickListener {
        void onSaveImage(Bitmap bitmap);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public View copy, share, saveImg;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.content);
            copy = itemView.findViewById(R.id.copy);
            share = itemView.findViewById(R.id.share);
            saveImg = itemView.findViewById(R.id.img_save);
        }

        public void bind() {

        }
    }
}

