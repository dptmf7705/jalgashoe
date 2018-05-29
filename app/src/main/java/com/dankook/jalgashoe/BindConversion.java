package com.dankook.jalgashoe;

import android.databinding.BindingConversion;
import android.util.Log;
import android.view.View;

public class BindConversion {

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible){
        return visible ? View.VISIBLE : View.GONE;
    }

    @BindingConversion
    public static String convertIntToString(int value){
        return String.valueOf(value);
    }
}

