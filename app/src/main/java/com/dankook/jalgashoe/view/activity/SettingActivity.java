package com.dankook.jalgashoe.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.util.CustomActionBar;

public class SettingActivity extends AppCompatActivity {
    private CustomActionBar customActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setupView();
    }

    private void setupView(){
        customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.removeSettingButton();
        customActionBar.setBackButton();
    }
}
