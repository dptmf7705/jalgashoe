package com.dankook.jalgashoe.data.vo;

/**
 * Created by yeseul on 2018-04-11.
 */

public class SettingItemVO {
    private int settingItemImage;
    private String settingItemText;

    public SettingItemVO() {
    }

    public SettingItemVO(int settingItemImage, String settingItemText) {
        this.settingItemImage = settingItemImage;
        this.settingItemText = settingItemText;
    }

    public int getSettingItemImage() {
        return settingItemImage;
    }

    public void setSettingItemImage(int settingItemImage) {
        this.settingItemImage = settingItemImage;
    }

    public String getSettingItemText() {
        return settingItemText;
    }

    public void setSettingItemText(String settingItemText) {
        this.settingItemText = settingItemText;
    }
}
