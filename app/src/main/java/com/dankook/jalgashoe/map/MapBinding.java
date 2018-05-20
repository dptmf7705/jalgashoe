package com.dankook.jalgashoe.map;

import android.databinding.BindingAdapter;
import android.widget.ImageButton;

import com.dankook.jalgashoe.R;

/**
 * Created by yeseul on 2018-05-20.
 */

public class MapBinding {

    @BindingAdapter(value = {"compassMode"})
    public static void compassButtonImageBinding(ImageButton button, boolean bool){
        if(bool) {
            button.setImageResource(R.drawable.ic_compass_active);
        } else {
            button.setImageResource(R.drawable.ic_compass_deactive);
        }
    }

    @BindingAdapter(value = {"myLocation"})
    public static void locationButtonImageBinding(ImageButton button, boolean bool){
        if(bool) {
            button.setImageResource(R.drawable.ic_current_location);
        } else {
            button.setImageResource(R.drawable.ic_my_location);
        }
    }
}
