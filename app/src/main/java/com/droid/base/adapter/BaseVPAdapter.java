package com.droid.base.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.droid.base.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by wungko on 16/8/1.
 */
public class BaseVPAdapter extends FragmentPagerAdapter {
    ArrayList<BaseFragment> mFragments;

    public BaseVPAdapter(FragmentManager fm, ArrayList<BaseFragment> fs) {
        super(fm);
        mFragments = fs;

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }
}
