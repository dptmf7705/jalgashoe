package com.dankook.jalgashoe.data.vo;

/**
 * Created by yeseul on 2018-04-11.
 */

public class LoginVO {
    private String userId;
    private String userPassword;

    public LoginVO() {
    }

    public LoginVO(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
