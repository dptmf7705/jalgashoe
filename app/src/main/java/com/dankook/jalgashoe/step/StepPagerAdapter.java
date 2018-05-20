package com.dankook.jalgashoe.step;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dankook.jalgashoe.BasePagerAdapter;
import com.dankook.jalgashoe.excercise.DayFragment;
import com.dankook.jalgashoe.excercise.WeekFragment;
import com.dankook.jalgashoe.excercise.MonthFragment;
import com.dankook.jalgashoe.excercise.YearFragment;

import static com.dankook.jalgashoe.util.Constant.STEP_OPTION_COUNT;

/**
 * Created by yeseul on 2018-03-24.
 */

public class StepPagerAdapter extends BasePagerAdapter {

    public StepPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(context, fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new DayFragment();
                break;
            case 1:
                fragment = new WeekFragment();
                break;
            case 2:
                fragment = new MonthFragment();
                break;
            case 3:
                fragment = new YearFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return STEP_OPTION_COUNT;
    }
}
