package com.dankook.jalgashoe.map;

/**
 * Created by yeseul on 2018-05-02.
 */

public interface MapNavigator {

    void showSnackBar(String message);

    void startSearchActivity();

    void startPathActivity(String s);
}
