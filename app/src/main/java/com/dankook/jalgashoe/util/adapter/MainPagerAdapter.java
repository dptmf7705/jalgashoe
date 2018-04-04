package com.dankook.jalgashoe.util.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dankook.jalgashoe.view.fragment.M3StepFragment;
import com.dankook.jalgashoe.view.fragment.M1MapFragment;
import com.dankook.jalgashoe.view.fragment.M4ProfileFragment;
import com.dankook.jalgashoe.view.fragment.M2ExerciseFragment;

import static com.dankook.jalgashoe.util.Constant.TAB_COUNT;

/**
 * Created by yeseul on 2018-04-04.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public MainPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new M1MapFragment();
                break;
            case 1:
                fragment = new M2ExerciseFragment();
                break;
            case 2:
                fragment = new M3StepFragment();
                break;
            case 3:
                fragment = new M4ProfileFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
