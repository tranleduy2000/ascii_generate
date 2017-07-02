package com.duy.acsiigenerator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.duy.acsiigenerator.figlet.TextFragment;
import com.duy.acsiigenerator.image.ImageToAsciiFragment;

/**
 * Created by Duy on 15-Jun-17.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
    private TextFragment textFragment;
    private ImageToAsciiFragment imageToAsciiFragment;

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
        imageToAsciiFragment = ImageToAsciiFragment.newInstance();
        textFragment = TextFragment.newInstance();
    }

    public TextFragment getTextFragment() {
        return textFragment;
    }

    public ImageToAsciiFragment getImageToAsciiFragment() {
        return imageToAsciiFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return textFragment;
        } else if (position == 1) {
            return imageToAsciiFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "TEXT";
        } else if (position == 1) {
            return "IMAGE";
        }
        return super.getPageTitle(position);
    }
}
