package com.dankook.jalgashoe.searchPoi;

import com.skt.Tmap.TMapPoint;

/**
 * Created by yeseul on 2018-05-08.
 */

public interface SearchActivityNavigator {

    void onActivityFinish();

    void showSnackBar(String message);

    void finishActivityWithResult(int resultCode, TMapPoint poiPoint);

    void changeToAutoSearchFragment();

    void changeToPoiListFragment();
}
