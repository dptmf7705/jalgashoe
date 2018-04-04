package com.dankook.jalgashoe.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dankook.jalgashoe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class X3MonthFragment extends Fragment {


    public X3MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.x3_fragment_month, container, false);
    }

}
