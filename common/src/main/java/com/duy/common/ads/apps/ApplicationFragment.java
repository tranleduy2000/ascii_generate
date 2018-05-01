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

package com.duy.common.ads.apps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duy.common.R;
import com.duy.common.utils.StoreUtil;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Duy on 26-Dec-17.
 */

public class ApplicationFragment extends Fragment {
    private static final String KEY_APP_ITEM = "KEY_APP_ITEM";

    public static ApplicationFragment newInstance(ApplicationItem applicationItem) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_APP_ITEM, applicationItem);
        ApplicationFragment fragment = new ApplicationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_application, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView wallpaper = view.findViewById(R.id.img_wallpaper);
        ImageView icon = view.findViewById(R.id.img_icon);
        final ApplicationItem applicationItem = (ApplicationItem) getArguments().getSerializable(KEY_APP_ITEM);
        Glide.with(getContext()).load(applicationItem.getIconUrl()).apply(new RequestOptions().centerCrop()).into(icon);
        Glide.with(getContext()).load(applicationItem.getWallpaperUrl()).apply(new RequestOptions().centerCrop()).into(wallpaper);

        TextView txtName = view.findViewById(R.id.txt_name);
        txtName.setText(applicationItem.getName());

        view.findViewById(R.id.root_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String applicationId = applicationItem.getApplicationId();
                FirebaseAnalytics.getInstance(getActivity()).logEvent(applicationId, new Bundle());
                StoreUtil.gotoPlayStore(getActivity(), applicationId);
            }
        });
    }

}
