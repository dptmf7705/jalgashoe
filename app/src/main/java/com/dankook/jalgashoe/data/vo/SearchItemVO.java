package com.dankook.jalgashoe.data.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dankook.jalgashoe.BR;

/**
 * Created by yeseul on 2018-05-20.
 */

public class SearchItemVO extends BaseObservable{
    private int id;
    private String text;
    private String date;

    public SearchItemVO(){

    }

    public SearchItemVO(int id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }
}
