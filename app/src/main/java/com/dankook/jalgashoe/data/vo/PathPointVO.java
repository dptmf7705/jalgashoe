package com.dankook.jalgashoe.data.vo;

import com.skt.Tmap.TMapPoint;

/**
 * Created by yeseul on 2018-05-21.
 */

public class PathPointVO extends PathVO {
    private int pointIndex; // point 순서
    private int turnType; // 회전 정보 구분
    private String pointType; // 안내 지점 구분
    private String direction; // 방면 명칭(동일로 방면)
    private String nearPoiName; // 안내점 기준 poi 명칭
    private TMapPoint nearPoiPoint; // 안내점 기준 poi 포인트
    private String intersectName; // 교차로 명칭

    public int getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(int pointIndex) {
        this.pointIndex = pointIndex;
    }

    public int getTurnType() {
        return turnType;
    }

    public void setTurnType(int turnType) {
        this.turnType = turnType;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getNearPoiName() {
        return nearPoiName;
    }

    public void setNearPoiName(String nearPoiName) {
        this.nearPoiName = nearPoiName;
    }

    public TMapPoint getNearPoiPoint() {
        return nearPoiPoint;
    }

    public void setNearPoiPoint(TMapPoint nearPoiPoint) {
        this.nearPoiPoint = nearPoiPoint;
    }

    public String getIntersectName() {
        return intersectName;
    }

    public void setIntersectName(String intersectName) {
        this.intersectName = intersectName;
    }
}
