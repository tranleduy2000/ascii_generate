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

package com.duy.ascii.art.unicodesymbol;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.R;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;

import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

class SymbolAdapter extends RecyclerView.Adapter<SymbolAdapter.ViewHolder> {
    protected LayoutInflater inflater;
    private Context context;
    private ClipboardManagerCompat clipboardManagerCompat;
    private ArrayList<String> symbols;
    @Nullable
    private SymbolClickListener mListener;

    SymbolAdapter(@NonNull Context context, ArrayList<String> symbols) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.symbols = symbols;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtContent.setText(symbols.get(position));
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboardManagerCompat.setText(holder.txtContent.getText().toString());
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });
        holder.txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onClick(symbols.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return symbols.size();
    }

    public void setListener(@Nullable SymbolClickListener listener) {
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.text);
        }

    }
}

