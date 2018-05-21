package com.dankook.jalgashoe.data.vo;

import com.skt.Tmap.TMapPoint;

import java.util.List;

/**
 * Created by yeseul on 2018-05-21.
 */

public class PathVO {
    protected int index; // path 순서
    protected String type; // point or line
    protected String name; // 지점 or 도로 이름
    protected String description; // 길안내 설명
    protected List<TMapPoint> points; // 포인트 (lat, lon)
    protected int facilityType; // 시설물 정보
    protected String facilityName; // 시설물 이름

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TMapPoint> getPoints() {
        return points;
    }

    public void setPoints(List<TMapPoint> points) {
        this.points = points;
    }

    public int getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(int facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
}
