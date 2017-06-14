package com.duy.acsiigenerator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.duy.acsiigenerator.figlet.ConvertFragment;
import com.duy.acsiigenerator.image.ImageToAsciiFragment;

/**
 * Created by Duy on 15-Jun-17.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ConvertFragment.newInstance();
        } else if (position == 1) {
            return ImageToAsciiFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
