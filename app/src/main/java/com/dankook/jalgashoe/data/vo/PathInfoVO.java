package com.dankook.jalgashoe.data.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dankook.jalgashoe.BR;
import com.skt.Tmap.TMapPoint;

/**
 * Created by yeseul on 2018-05-22.
 */

public class PathInfoVO extends BaseObservable {
    private TMapPoint startPoint;
    private TMapPoint endPoint;
    private String startAddress;
    private String endAddress;
    private String lineDistance;
    private String pathDistance;
    private String pathTime;

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
