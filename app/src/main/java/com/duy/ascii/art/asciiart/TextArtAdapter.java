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

package com.duy.ascii.art.asciiart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.BuildConfig;
import com.duy.ascii.art.R;
import com.duy.ascii.art.asciiart.model.TextArt;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.favorite.localdata.DatabasePresenter;
import com.duy.ascii.art.favorite.localdata.TextItem;
import com.duy.ascii.art.utils.ShareUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Duy on 9/27/2017.
 */

public class TextArtAdapter extends RecyclerView.Adapter<TextArtAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<TextArt> mTextArts = new ArrayList<>();
    private LayoutInflater miInflater;
    private ClipboardManagerCompat mClipboard;
    private DatabasePresenter mDatabasePresenter;
    private OnItemClickListener listener;


    public TextArtAdapter(Context context) {
        this.mContext = context;
        this.miInflater = LayoutInflater.from(context);
        this.mClipboard = ClipboardManagerCompatFactory.getManager(context);
        this.mDatabasePresenter = new DatabasePresenter(context, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = miInflater.inflate(R.layout.list_item_emoji_recent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        TextArt item = mTextArts.get(position);
        final String text = item.getContent();
        final TextArt textArt = item;

        holder.txtContent.setText(text);
        holder.txtName.setText(item.getName() + " " + item.getTime());
        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipboard.setText(holder.txtContent.getText().toString());
                Toast.makeText(mContext, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(holder.txtContent.getText().toString(), mContext);
            }
        });
        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabasePresenter.insert(new TextItem(text));
                Toast.makeText(mContext, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
            }
        });

        if (BuildConfig.DEBUG) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(textArt);
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnDelete.setOnClickListener(null);
        }
    }

    private void delete(TextArt textArt) {
        if (listener != null) {
            listener.onDelete(textArt);
        }
        int index = mTextArts.indexOf(textArt);
        if (index >= 0) {
            mTextArts.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public int getItemCount() {
        return mTextArts.size();
    }

    public void add(TextArt value) {
        mTextArts.add(value);
        notifyItemInserted(mTextArts.size() - 1);
    }

    public TextArt getLastItem() {
        return mTextArts.get(mTextArts.size() - 1);
    }

    public TextArt getFirstItem() {
        return mTextArts.get(0);
    }

    public void addAll(List<TextArt> textArts) {
        //sort decrease
        Collections.sort(textArts, new Comparator<TextArt>() {
            @Override
            public int compare(TextArt textArt, TextArt t1) {
                return -textArt.getTime().compareTo(t1.getTime());
            }
        });
        mTextArts.addAll(textArts);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mTextArts.clear();
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onDelete(TextArt textArt);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtName;
        View imgCopy, imgShare, imgFavorite, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtName = itemView.findViewById(R.id.txt_name);
            imgCopy = itemView.findViewById(R.id.img_copy);
            imgShare = itemView.findViewById(R.id.img_share);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
