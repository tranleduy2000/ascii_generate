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

import com.duy.ascii.sharedcode.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Duy on 06-May-17.
 */

 class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    private static final String PATH = "emoticons";
    private final ArrayList<String> objects = new ArrayList<>();
    protected LayoutInflater inflater;
    private Context context;
    private HeaderClickListener listener;


    public HeaderAdapter(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        try {
            String[] list = context.getAssets().list(PATH);
            Collections.addAll(objects, list);
            Collections.sort(objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_header_emoticons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String text = objects.get(position);
        holder.txtContent.setText(refine(text));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onHeaderClick(PATH + "/" + text);
            }
        });
    }

    public static String refine(String text) {
        if (text.contains(".")) {
            text = text.substring(0, text.indexOf("."));
        }
        String name = Character.toUpperCase(text.charAt(0)) + "";
        for (int i = 1; i < text.length(); i++) {
            if (Character.isUpperCase(text.charAt(i))) {
                name += " " + text.charAt(i);
            } else {
                name += text.charAt(i);
            }
        }
        return name;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setListener(HeaderClickListener listener) {
        this.listener = listener;
    }

    public interface HeaderClickListener {
        void onHeaderClick(String path);
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

