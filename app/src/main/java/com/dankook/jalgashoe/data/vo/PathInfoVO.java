package com.dankook.jalgashoe.data.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dankook.jalgashoe.BR;
import com.skt.Tmap.TMapPoint;

/**
 * Created by yeseul on 2018-05-22.
 */

public class PathInfoVO extends BaseObservable {
    private TMapPoint startPoint; // 출발지 위, 경도
    private TMapPoint endPoint; // 도착지 위, 경도
    private String startAddress; // 출발지 주소
    private String endAddress; // 도착지 주소
    private String lineDistance; // 직선 거리
    private String pathDistance; // 보행 거리
    private String pathTime; // 예상 소요 시간

    @Bindable
    public TMapPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(TMapPoint startPoint) {
        this.startPoint = startPoint;
        notifyPropertyChanged(BR.startPoint);
    }

    @Bindable
    public TMapPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(TMapPoint endPoint) {
        this.endPoint = endPoint;
        notifyPropertyChanged(BR.endPoint);
    }

    @Bindable
    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
        notifyPropertyChanged(BR.startAddress);
    }

    @Bindable
    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
        notifyPropertyChanged(BR.endAddress);
    }

    @Bindable
    public String getLineDistance() {
        return lineDistance;
    }

    public void setLineDistance(String lineDistance) {
        this.lineDistance = lineDistance;
        notifyPropertyChanged(BR.lineDistance);
    }

    @Bindable
    public String getPathDistance() {
        return pathDistance;
    }

    public void setPathDistance(String pathDistance) {
        this.pathDistance = pathDistance;
        notifyPropertyChanged(BR.pathDistance);
    }

    @Bindable
    public String getPathTime() {
        return pathTime;
    }

    public void setPathTime(String pathTime) {
        this.pathTime = pathTime;
        notifyPropertyChanged(BR.pathTime);
    }
}
