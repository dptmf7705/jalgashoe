package com.dankook.jalgashoe.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.util.CustomActionBar;

public class JoinActivity extends AppCompatActivity {
    private CustomActionBar customActionBar;
    private Button buttonSubmit;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        customActionBar = new CustomActionBar(this, getSupportActionBar());
        customActionBar.removeSettingButton();
        customActionBar.setBackButton();

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
