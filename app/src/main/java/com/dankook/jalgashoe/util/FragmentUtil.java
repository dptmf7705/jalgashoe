package com.dankook.jalgashoe.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dankook.jalgashoe.R;

/**
 * Created by yeseul on 2018-05-20.
 */

public class FragmentUtil {

    public static void changeFragment(FragmentManager fragmentManager, int layoutId, Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.commit();
    }

}
