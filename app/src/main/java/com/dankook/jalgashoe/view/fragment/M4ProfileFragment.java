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
public class M4ProfileFragment extends Fragment {


    public M4ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.m4_fragment_profile, container, false);
    }

}
