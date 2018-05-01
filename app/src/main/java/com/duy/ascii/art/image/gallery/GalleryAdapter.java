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

package com.duy.ascii.art.image.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duy.ascii.art.R;
import com.duy.ascii.art.utils.ShareUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Duy on 09-Aug-17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<File> paths = new ArrayList<>();

    public GalleryAdapter(Context context, ArrayList<File> paths) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.paths = paths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(context).load(paths.get(position))
                .apply(new RequestOptions().centerCrop())
                .into(holder.imageView);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareImage(context, paths.get(holder.getAdapterPosition()));
            }
        });
    }

    private void remove(int adapterPosition) {
        try {
            File remove = paths.remove(adapterPosition);
            if (remove.delete()) {
                notifyItemRemoved(adapterPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button btnDelete, btnShare;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }
}
