package com.dankook.jalgashoe.util.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dankook.jalgashoe.view.fragment.X1DayFragment;
import com.dankook.jalgashoe.view.fragment.X2WeekFragment;
import com.dankook.jalgashoe.view.fragment.X3MonthFragment;
import com.dankook.jalgashoe.view.fragment.X4YearFragment;

import static com.dankook.jalgashoe.util.Constant.STEP_OPTION_COUNT;

/**
 * Created by yeseul on 2018-03-24.
 */

public class StepPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public StepPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new X1DayFragment();
                break;
            case 1:
                fragment = new X2WeekFragment();
                break;
            case 2:
                fragment = new X3MonthFragment();
                break;
            case 3:
                fragment = new X4YearFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return STEP_OPTION_COUNT;
    }
}
