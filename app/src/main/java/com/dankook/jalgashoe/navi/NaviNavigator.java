package com.dankook.jalgashoe.navi;

import com.dankook.jalgashoe.data.vo.PathPointVO;

/**
 * Created by yeseul on 2018-05-22.
 */

public interface NaviNavigator {
    void showSnackBar(String message);

    void startNavigation();

    void onNextPath(PathPointVO vo);

    void finishNavigation();
}
