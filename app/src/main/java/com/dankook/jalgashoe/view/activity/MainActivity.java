package com.dankook.jalgashoe.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.util.CustomActionBar;
import com.dankook.jalgashoe.util.CustomViewPager;
import com.dankook.jalgashoe.util.MainPageTransformer;
import com.dankook.jalgashoe.util.adapter.MainPagerAdapter;

import static com.dankook.jalgashoe.util.Constant.TAB_COUNT;
import static com.dankook.jalgashoe.util.Constant.TAB_ICON;

public class MainActivity extends AppCompatActivity {
    private CustomActionBar customActionBar;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private MainPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        setupPager();
    }

    private void setupView(){
        customActionBar = new CustomActionBar(this, getSupportActionBar());
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void setupPager(){
        adapter = new MainPagerAdapter(this, getSupportFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setPageTransformer(true, new MainPageTransformer());
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        for(int i = 0 ; i < TAB_COUNT ; i++){
            View view = getLayoutInflater().inflate(R.layout.layout_main_tab, null);
            ImageView icon = view.findViewById(R.id.icon);
            icon.setImageResource(TAB_ICON[i]);
            tabLayout.getTabAt(i).setCustomView(view);
        }
    }
}
