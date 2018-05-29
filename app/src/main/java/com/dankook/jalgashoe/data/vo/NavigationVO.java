package com.dankook.jalgashoe.data.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dankook.jalgashoe.BR;
import com.skt.Tmap.TMapPoint;

public class NavigationVO extends BaseObservable {
    private String endDistance; // 남은 거리
    private String endTime; // 도착 시간
    private TMapPoint turnPoint; // 교차점
    private int turnType; // 회전 정보
    private int turnDistance; // 교차로까지 거리
    private String turnName; // 교차로 이름

    @Bindable
    public String getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(String endDistance) {
        this.endDistance = endDistance;
        notifyPropertyChanged(BR.endDistance);
    }

    @Bindable
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        notifyPropertyChanged(BR.endTime);
    }

    @Bindable
    public TMapPoint getTurnPoint() {
        return turnPoint;
    }

    public void setTurnPoint(TMapPoint turnPoint) {
        this.turnPoint = turnPoint;
        notifyPropertyChanged(BR.turnPoint);
    }

    @Bindable
    public int getTurnType() {
        return turnType;
    }

    public void setTurnType(int turnType) {
        this.turnType = turnType;
        notifyPropertyChanged(BR.turnType);
    }

    @Bindable
    public int getTurnDistance() {
        return turnDistance;
    }

    public void setTurnDistance(int turnDistance) {
        this.turnDistance = turnDistance;
        notifyPropertyChanged(BR.turnDistance);
    }

    @Bindable
    public String getTurnName() {
        return turnName;
    }

    public void setTurnName(String turnName) {
        this.turnName = turnName;
        notifyPropertyChanged(BR.turnName);
    }
}
