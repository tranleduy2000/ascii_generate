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

package com.duy.ascii.sharedcode.emoji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.ascii.sharedcode.R;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompat;
import com.duy.ascii.sharedcode.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.sharedcode.emoji.HeaderAdapter.EmojiClickListener;

import java.util.ArrayList;

/**
 * Created by Duy on 09-Aug-17.
 */

public class EmojiFragment extends BottomSheetDialogFragment {
    public static final String TAG = "EmojiFragment";

    public static EmojiFragment newInstance(ArrayList<String> emoji) {

        Bundle args = new Bundle();
        args.putSerializable("data", emoji);
        EmojiFragment fragment = new EmojiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), getActivity().getTheme());
        return LayoutInflater.from(contextThemeWrapper).inflate(R.layout.fragment_emoji, container, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> emojis = (ArrayList<String>) getArguments().getSerializable("data");

        final EditText editInput = view.findViewById(R.id.edit_input);
        Button btnCopy = view.findViewById(R.id.btn_copy);

        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_emoji);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        EmojiAdapter emojiAdapter = new EmojiAdapter(getActivity(), emojis);
        recyclerView.setAdapter(emojiAdapter);

        emojiAdapter.setListener(new EmojiClickListener() {
            @Override
            public void onClick(String emoji) {
                editInput.getEditableText().insert(Math.max(editInput.getSelectionStart(), 0), emoji);
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManagerCompat manager = ClipboardManagerCompatFactory.getManager(getContext());
                manager.setText(editInput.getText());
                Toast.makeText(getContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
