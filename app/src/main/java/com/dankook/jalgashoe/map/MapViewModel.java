package com.dankook.jalgashoe.map;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.searchPoi.SearchActivity;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by yeseul on 2018-05-02.
 */

public class MapViewModel {

    public static final String MARKER_DEPARTURE = "MARKER_DEPARTURE";
    public static final String MARKER_DESTINATION = "MARKER_DESTINATION";

    public final ObservableField<String> currentAddress = new ObservableField<>("현재 위치 찾는중 ...");
    public final ObservableField<String> departureAddress = new ObservableField<>();
    public final ObservableField<String> destinationAddress = new ObservableField<>();

    public final ObservableBoolean isCompassMode = new ObservableBoolean(false);
    public final ObservableBoolean isMyLocation = new ObservableBoolean(false);

    private MapNavigator navigator;

    private TMapView mapView;
    private TMapGpsManager gpsManager;
    private TMapData tMapData;

    private TMapPoint departurePoint;
    private TMapPoint destinationPoint;

    // test code
    public final ObservableBoolean showPath = new ObservableBoolean(false);
    public final ObservableField<String> textPath = new ObservableField<>();
    public void onPathCloseClick(){
        showPath.set(false);
    }
    // test code end

    public void setNavigator(MapNavigator navigator) {
        this.navigator = navigator;
    }

    public void start(TMapView mapView, TMapGpsManager gpsManager) {
        this.mapView = mapView;
        this.gpsManager = gpsManager;
        this.tMapData = new TMapData();

        setupMapView();
        setupGpsManager();
    }

    private void setupMapView() {
        mapView.setSKTMapApiKey("b7bfb971-b45e-40d9-8edb-8b2b46bfb04d"); // key 설정
        mapView.setZoomLevel(18); // 지도 줌레벨 설정 (7~19)
        mapView.setIconVisibility(true); // 현재위치 아이콘 나타내기
        mapView.setMapType(TMapView.MAPTYPE_STANDARD); // 일반 지도 사용

        mapView.setCompassMode(false); // 나침반 모드 해제
        mapView.setSightVisible(false); // 시야 표출 해제
        isCompassMode.set(false);

        mapView.setTrackingMode(true); // 사용자 위치 따라서 이동하는 모드로

        mapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float v, TMapPoint tMapPoint) {
                if(isCompassMode.get()){ // 나침반 모드 실행중인경우 그대로 실행
                    mapView.setCompassMode(true);
                }
                isMyLocation.set(false);
            }
        });
    }

    // 위치 정보 설정
    private void setupGpsManager(){
        gpsManager.setMinTime(1000); // 위치 변경 인식 시간간격 설정
        gpsManager.setMinDistance(5); // 위치 변경 인식 최소거리 설정

        // GPS_PROVIDER : 위성 기반 위치탐색 (건물, 지하에서 탐색불가)
        // NETWORK_PROVIDER : 네트워크 기반 위치탐색
        gpsManager.setProvider(TMapGpsManager.GPS_PROVIDER);

        gpsManager.OpenGps(); // gps 추적 시작
    }

    public void changeCurrentLocation(TMapPoint location){
        changeDepartureToMyLocation(location);
        mapView.setCenterPoint(location.getLongitude(), location.getLatitude(), true); // 현재위치로 화면 이동
        mapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 현재 위치 변경
        isMyLocation.set(true);

        changeCurrentAddress(location.getLatitude(), location.getLongitude()); // 현위치 주소 변경
    }

    private void changeCurrentAddress(double latitude, double longitude){
        tMapData.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                currentAddress.set(s);
            }
        });
    }

    public void onMyLocationClick(){
        currentAddress.set("현재 위치 찾는중 ...");
        changeCurrentLocation(gpsManager.getLocation());
    }

    public void onCompassChange(){
        boolean bool = !(mapView.getIsCompass());

        mapView.setCompassMode(bool); // 나침반 모드
        mapView.setSightVisible(bool); // 시야 표출
        isCompassMode.set(bool);
    }

    private void getMarkerIcon(final TMapMarkerItem marker, int iconId){
        Glide.with(mapView.getContext())
                .load(iconId)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        marker.setIcon(resource);
                    }
                });
    }

    private void changeDepartureToMyLocation(TMapPoint location){

        departurePoint = location;

        tMapData.convertGpsToAddress(location.getLatitude(), location.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                departureAddress.set("현재 위치 : " + s);
            }
        });

        TMapMarkerItem marker = new TMapMarkerItem();
        getMarkerIcon(marker, R.drawable.ic_location_active);
        marker.setTMapPoint(departurePoint);

        if(mapView.getMarkerItemFromID(MARKER_DEPARTURE) != null) {
            mapView.removeMarkerItem(MARKER_DEPARTURE);
        }
        mapView.addMarkerItem(MARKER_DEPARTURE, marker);
    }

    private void changeDestinationToMyLocation(TMapPoint location){

        destinationPoint = location;

        tMapData.convertGpsToAddress(location.getLatitude(), location.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                departureAddress.set("현재 위치 : " + s);
            }
        });

        TMapMarkerItem marker = new TMapMarkerItem();
        getMarkerIcon(marker, R.drawable.ic_marker_destination);
        marker.setTMapPoint(destinationPoint);

        if(mapView.getMarkerItemFromID(MARKER_DESTINATION) != null) {
            mapView.removeMarkerItem(MARKER_DESTINATION);
        }
        mapView.addMarkerItem(MARKER_DESTINATION, marker);
    }

    private void changeDeparture(String name, double latitude, double longitude){

        departurePoint = new TMapPoint(latitude, longitude);

        tMapData.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                departureAddress.set(s);
            }
        });

        TMapMarkerItem marker = new TMapMarkerItem();
        getMarkerIcon(marker, R.drawable.ic_location_active);
        marker.setTMapPoint(departurePoint);
        marker.setName(name);

        if(mapView.getMarkerItemFromID(MARKER_DEPARTURE) != null) {
            mapView.removeMarkerItem(MARKER_DEPARTURE);
        }
        mapView.addMarkerItem(MARKER_DEPARTURE, marker);

        mapView.setCenterPoint(longitude, latitude);
    }

    private void changeDestination(String name, double latitude, double longitude){

        destinationPoint = new TMapPoint(latitude, longitude);

        tMapData.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                destinationAddress.set(s);
            }
        });

        TMapMarkerItem marker = new TMapMarkerItem();
        getMarkerIcon(marker, R.drawable.ic_marker_destination);
        marker.setTMapPoint(destinationPoint);
        marker.setName(name);

        if(mapView.getMarkerItemFromID(MARKER_DESTINATION) != null) {
            mapView.removeMarkerItem(MARKER_DESTINATION);
        }
        mapView.addMarkerItem(MARKER_DESTINATION, marker);

        mapView.setCenterPoint(longitude, latitude);
    }

    public void onClickSearchBar(){
        navigator.startSearchActivity();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchActivity.REQUEST_MAP_SEARCH){
            // 출발지로 설정됨
            if(resultCode == SearchActivity.RESULT_DEPARTURE){

                Bundle point = data.getBundleExtra(SearchActivity.BUNDLE_EXTRA_POINT);
                Double longitude = point.getDouble(SearchActivity.BUNDLE_EXTRA_LONGITUDE);
                Double latitude = point.getDouble(SearchActivity.BUNDLE_EXTRA_LATITUDE);
                String name = data.getStringExtra(SearchActivity.STRING_EXTRA_NAME);

                changeDeparture(name, latitude, longitude);

                drawPolyLine();
            }

            // 목적지로 설정됨
            if(resultCode == SearchActivity.RESULT_DESTINATION){

                Bundle point = data.getBundleExtra(SearchActivity.BUNDLE_EXTRA_POINT);
                Double longitude = point.getDouble(SearchActivity.BUNDLE_EXTRA_LONGITUDE);
                Double latitude = point.getDouble(SearchActivity.BUNDLE_EXTRA_LATITUDE);
                String name = data.getStringExtra(SearchActivity.STRING_EXTRA_NAME);

                changeDestination(name, latitude, longitude);

                drawPolyLine();
            }
        }
    }

    private void drawPolyLine() {
        if(destinationPoint != null && departurePoint != null){
            mapView.setIconVisibility(false);
/*

            TMapPolyLine polyLine = new TMapPolyLine();

            polyLine.addLinePoint(departurePoint);
            polyLine.addLinePoint(destinationPoint);

            polyLine.setLineColor(R.color.mainBlue);
            polyLine.setLineWidth(2);

            DashPathEffect dashPath = new DashPathEffect(new float[]{20,10}, 1); //점선
            polyLine.setPathEffect(dashPath);
*/

            // 보행자 경로
            tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, departurePoint, destinationPoint,
                    new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                    mapView.addTMapPolyLine("", tMapPolyLine);
                }
            });

            ArrayList<TMapPoint> list = new ArrayList<>();
            list.add(destinationPoint);
            list.add(departurePoint);
            TMapInfo info = mapView.getDisplayTMapInfo(list);

            mapView.setZoomLevel(info.getTMapZoomLevel());
            mapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
        }
    }

    public void onFindPathClick(){
        tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, departurePoint, destinationPoint,
                new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document document) {
                // 최상위 node
                Element root = document.getDocumentElement();

                NodeList placeMark = root.getElementsByTagName("Placemark");

                StringBuffer sb = new StringBuffer();

                for(int i = 0 ; i < placeMark.getLength() ; i++){

                    NodeList placeMarkItem = placeMark.item(i).getChildNodes();

                    for(int j = 0 ; j < placeMarkItem.getLength() ; j++){
                        if(placeMarkItem.item(j).getNodeName().equals("description")){
                            sb.append(placeMarkItem.item(j).getTextContent() + "\n");
                        }
                    }

                }

                textPath.set(sb.toString());
                showPath.set(true);
            }
        });
    }
}
