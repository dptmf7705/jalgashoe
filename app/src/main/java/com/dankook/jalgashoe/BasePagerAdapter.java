package com.dankook.jalgashoe;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by yeseul on 2018-04-18.
 */

public abstract class BasePagerAdapter extends FragmentPagerAdapter {

    protected Context context;

    public BasePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

}