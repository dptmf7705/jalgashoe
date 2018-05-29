package com.dankook.jalgashoe.data.vo;

/**
 * Created by yeseul on 2018-05-21.
 */

public class PathLineVO extends PathVO {
    private int lineIndex; // line 순서
    private int distance; // 구간 거리
    private int time; // 구간 소요시간
    private int roadType; // 도로 타입
    private int categoryRoadType; // 특화 거리 타입

    @Override
    public String toString() {
        return "PathLineVO{" +
                "lineIndex=" + lineIndex +
                ", index=" + index +
                ", distance=" + distance +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", name='" + name + '\'' +
                ", roadType=" + roadType +
                ", description='" + description + '\'' +
                ", categoryRoadType=" + categoryRoadType +
                ", points=" + points +
                ", facilityType=" + facilityType +
                ", facilityName='" + facilityName + '\'' +
                '}';
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRoadType() {
        return roadType;
    }

    public void setRoadType(int roadType) {
        this.roadType = roadType;
    }

    public int getCategoryRoadType() {
        return categoryRoadType;
    }

    public void setCategoryRoadType(int categoryRoadType) {
        this.categoryRoadType = categoryRoadType;
    }
}
