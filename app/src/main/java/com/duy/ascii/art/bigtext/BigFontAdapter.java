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

package com.duy.ascii.art.bigtext;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.favorite.localdata.DatabasePresenter;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.utils.ShareUtil;
import com.duy.ascii.art.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Duy on 06-May-17.
 */

class BigFontAdapter extends RecyclerView.Adapter<BigFontAdapter.ViewHolder> {
    private final List<String> objects = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private ClipboardManagerCompat clipboardManagerCompat;
    @Nullable
    private View emptyView;
    private DatabasePresenter mDatabasePresenter;

    BigFontAdapter(@NonNull Context context, @Nullable View emptyView) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.emptyView = emptyView;
        this.mDatabasePresenter = new DatabasePresenter(context, null);
        invalidateEmptyView();
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
        View view = inflater.inflate(R.layout.list_item_big_font, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String text = objects.get(position);
        holder.txtContent.setText(text);
        holder.txtContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, holder.txtContent.getText().toString());
                intent.setType("text/plain");
                context.startActivity(intent);
                return false;
            }
        });
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManagerCompat.setText(holder.txtContent.getText().toString());
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManagerCompat.setText(holder.txtContent.getText().toString());
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(holder.txtContent.getText().toString(), context);
            }
        });
        holder.shareMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareMessenger(holder.txtContent.getText().toString(), context);
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
        objects.clear();
        notifyDataSetChanged();
        invalidateEmptyView();
    }

    public void add(String value) {
        this.objects.add(value);
        notifyItemInserted(objects.size() - 1);
        invalidateEmptyView();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        View imgCopy, imgShare, shareMsg, imgFavorite;

        ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
            imgCopy = itemView.findViewById(R.id.img_copy);
            imgShare = itemView.findViewById(R.id.img_share);
            shareMsg = itemView.findViewById(R.id.img_share_msg);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
        }

    }
}

