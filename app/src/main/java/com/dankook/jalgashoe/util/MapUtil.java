package com.dankook.jalgashoe.util;

import android.databinding.ObservableField;

import com.dankook.jalgashoe.data.vo.PathInfoVO;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class MapUtil extends TMapData {

    public static final int START_ADDRESS = 0;
    public static final int END_ADDRESS = 1;

    private static TMapData tMapData = new TMapData();

    public static void changeCurrentAddress(final ObservableField<String> address, TMapPoint point){
        tMapData.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                address.set(s);
            }
        });
    }

    public static void changeCurrentLocation(TMapView tMapView, TMapPoint location){
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude(), true); // 현재위치로 화면 이동
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 현재 위치 변경
    }

    public static void convertGpsToAddress(final PathInfoVO pathInfo, TMapPoint location, final boolean isCurrent, final int type){
        tMapData.convertGpsToAddress(location.getLatitude(), location.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                if(type == START_ADDRESS){

                    if(isCurrent) {
                        pathInfo.setStartAddress("현재 위치 : " + s);
                    } else {
                        pathInfo.setStartAddress(s);
                    }

                } else if (type == END_ADDRESS){

                    if(isCurrent) {
                        pathInfo.setEndAddress("현재 위치 : " + s);
                    } else {
                        pathInfo.setEndAddress(s);
                    }

                }

            }
        });
    }


}
