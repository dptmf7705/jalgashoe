package com.dankook.jalgashoe.map.path;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dankook.jalgashoe.BaseActivity;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.databinding.ActivityMapPathBinding;

/**
 * Created by yeseul on 2018-05-09.
 */

public class MapPathActivity extends BaseActivity<ActivityMapPathBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_path;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String document = intent.getStringExtra("document");

        binding.textView.setText(document);

    }
}
