package com.dankook.jalgashoe.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.view.activity.SettingActivity;


/**
 * Created by yeseul on 2018-03-23.
 */

public class CustomActionBar implements View.OnClickListener{
    private Activity activity;
    private ActionBar actionBar;
    private View view;
    private ImageButton buttonSetting;
    private ImageButton buttonBack;

    public CustomActionBar(AppCompatActivity activity, ActionBar actionBar){
        this.activity = activity;
        this.actionBar = actionBar;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);     //액션바 아이콘을 업 네비게이션 형태로 표시
        actionBar.setDisplayShowTitleEnabled(false);    //액션바에 표시되는 제목의 표시유무 설정
        actionBar.setDisplayShowHomeEnabled(false);     //홈 아이콘 숨김처리

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_actionbar, null);

        buttonSetting = view.findViewById(R.id.buttonSetting);
        buttonSetting.setOnClickListener(this);

        actionBar.setCustomView(view); //커스텀 뷰 적용

        // 액션바 padding 없애기
        Toolbar parent = (Toolbar)view.getParent();
        parent.setContentInsetsAbsolute(0,0);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ImageButton getButtonSetting() {
        return buttonSetting;
    }

    public void setButtonSetting(ImageButton buttonSetting) {
        this.buttonSetting = buttonSetting;
    }

    private void commit(){
        actionBar.setCustomView(view);
    }

    public void removeSettingButton(){
        buttonSetting.setVisibility(View.INVISIBLE);
        commit();
    }

    public void setBackButton(){
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setImageResource(R.mipmap.icon_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        commit();
    }
}
