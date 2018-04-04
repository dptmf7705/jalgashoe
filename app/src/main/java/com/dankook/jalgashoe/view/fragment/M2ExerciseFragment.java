package com.dankook.jalgashoe.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.util.adapter.StepPagerAdapter;

import static com.dankook.jalgashoe.util.Constant.STEP_OPTION_COUNT;
import static com.dankook.jalgashoe.util.Constant.STEP_OPTION_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class M2ExerciseFragment extends Fragment {
    private Context context;
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private StepPagerAdapter adapter;

    public M2ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.m2_fragment_exercise, container, false);
        context = view.getContext();

        setupView();
        setupPager();

        return view;
    }

    private void setupView(){
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }

    private void setupPager(){
        adapter = new StepPagerAdapter(context, getChildFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        for(int i = 0 ; i < STEP_OPTION_COUNT ; i++){
            tabLayout.getTabAt(i).setText(STEP_OPTION_NAME[i]);
        }
    }
}
