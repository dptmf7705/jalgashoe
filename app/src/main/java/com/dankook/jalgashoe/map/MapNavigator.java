package com.dankook.jalgashoe.map;

import com.skt.Tmap.TMapPoint;

/**
 * Created by yeseul on 2018-05-02.
 */

public interface MapNavigator {

    void showSnackBar(String message);

    void startSearchActivity();

    void startNaviActivity(TMapPoint startPoint, TMapPoint endPoint);
}
