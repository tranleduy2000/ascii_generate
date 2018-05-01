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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Duy on 26-Dec-17.
 */

public class ApplicationPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ArrayList<ApplicationItem> items;

    public ApplicationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.items = ApplicationHelper.getAllPackage(context);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ApplicationFragment.newInstance(items.get(position));
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
