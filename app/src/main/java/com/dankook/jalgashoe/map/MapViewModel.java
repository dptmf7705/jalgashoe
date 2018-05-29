package com.dankook.jalgashoe.map;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.vo.PathInfoVO;
import com.dankook.jalgashoe.searchPoi.SearchActivity;
import com.dankook.jalgashoe.util.BitmapUtil;
import com.dankook.jalgashoe.util.DateUtil;
import com.dankook.jalgashoe.util.MapUtil;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_TOTAL_DISTANCE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_TOTAL_TIME;

/**
 * Created by yeseul on 2018-05-02.
 */

public class MapViewModel {

    public final ObservableField<String> currentAddress = new ObservableField<>("현재 위치 찾는중 ...");
    public final ObservableBoolean isCompassMode = new ObservableBoolean(false);
    public final ObservableBoolean isMyLocation = new ObservableBoolean(false);
    public final ObservableBoolean showNext = new ObservableBoolean(false);

    private MapNavigator navigator;

    private TMapView mapView;
    private TMapGpsManager gpsManager;
    private TMapData tMapData;

    private PathInfoVO pathInfo = new PathInfoVO();

    public void setNavigator(MapNavigator navigator) {
        this.navigator = navigator;
    }

    public PathInfoVO getPathInfo() {
        return pathInfo;
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

        Bitmap startIcon = BitmapUtil.writeTextOnDrawable(mapView.getContext().getResources(), R.drawable.ic_location_active, 130, "출발");
        Bitmap endIcon = BitmapUtil.writeTextOnDrawable(mapView.getContext().getResources(), R.drawable.ic_marker_destination, 130, "도착");
        mapView.setTMapPathIcon(startIcon, endIcon);
    }

    // 위치 정보 설정
    private void setupGpsManager(){
        gpsManager.setMinTime(100); // 위치 변경 인식 시간간격 설정
        gpsManager.setMinDistance(0); // 위치 변경 인식 최소거리 설정

        // GPS_PROVIDER : 위성 기반 위치탐색 (건물, 지하에서 탐색불가)
        // NETWORK_PROVIDER : 네트워크 기반 위치탐색
        gpsManager.setProvider(TMapGpsManager.NETWORK_PROVIDER);

         gpsManager.OpenGps(); // gps 추적 시작
    }

    // 현재 위치 변경
    public void changeCurrentLocation(TMapPoint location){
        // 출발지가 설정되지 않은 경우 현재위치를 출발지로 설정한다
        if(pathInfo.getStartAddress() == null) {
            changeDeparture(location, true);
            gpsManager.CloseGps();
        }
        MapUtil.changeCurrentLocation(mapView, location);
        MapUtil.changeCurrentAddress(currentAddress, location);
        isMyLocation.set(true);
    }

    public void onMyLocationClick(){
        currentAddress.set("현재 위치 찾는중 ...");
        changeCurrentLocation(gpsManager.getLocation());
    }

    public void onCompassChange(){
        boolean bool = !(mapView.getIsCompass()); // 나침반 모드 토글

        mapView.setCompassMode(bool); // 나침반 모드 설정, 해제
        mapView.setSightVisible(bool); // 시야 표출 설정, 해제
        isCompassMode.set(bool);
    }

    private void changeDeparture(TMapPoint location, final boolean isCurrent){
        pathInfo.setStartPoint(location);
        mapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        MapUtil.convertGpsToAddress(pathInfo, location, isCurrent, MapUtil.START_ADDRESS);
    }

    private void changeDestination(TMapPoint location, final boolean isCurrent){
        pathInfo.setEndPoint(location);
        mapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        MapUtil.convertGpsToAddress(pathInfo, location, isCurrent, MapUtil.END_ADDRESS);
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

                changeDeparture(new TMapPoint(latitude, longitude), false);

                drawPolyLine();
            }

            // 목적지로 설정됨
            if(resultCode == SearchActivity.RESULT_DESTINATION){

                Bundle point = data.getBundleExtra(SearchActivity.BUNDLE_EXTRA_POINT);
                Double longitude = point.getDouble(SearchActivity.BUNDLE_EXTRA_LONGITUDE);
                Double latitude = point.getDouble(SearchActivity.BUNDLE_EXTRA_LATITUDE);
                String name = data.getStringExtra(SearchActivity.STRING_EXTRA_NAME);

                changeDestination(new TMapPoint(latitude, longitude), false);

                drawPolyLine();
            }
        }
    }

    private void drawPolyLine() {
        if(pathInfo.getEndPoint() != null && pathInfo.getStartPoint() != null){
            // 보행자 경로
            tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                    pathInfo.getStartPoint(), pathInfo.getEndPoint(),
                    new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                    DashPathEffect dashPath = new DashPathEffect(new float[]{20,10}, 1); //점선

                    tMapPolyLine.setLineColor(Color.BLACK);
                    tMapPolyLine.setLineWidth(10);
                    tMapPolyLine.setLineAlpha(170);
                    tMapPolyLine.setPathEffect(dashPath);

                    tMapPolyLine.setOutLineAlpha(0);

                    mapView.addTMapPath(tMapPolyLine);
                    showNext.set(true);
                }
            });

            // 출발지, 도착지, 경로선을 화면안에 표시
            ArrayList<TMapPoint> list = new ArrayList<>();
            list.add(pathInfo.getStartPoint());
            list.add(pathInfo.getEndPoint());
            TMapInfo info = mapView.getDisplayTMapInfo(list);

            mapView.setZoomLevel(info.getTMapZoomLevel() - 1);
            mapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());

            // 거리, 시간 정보 파싱
            tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                    pathInfo.getStartPoint(), pathInfo.getEndPoint(),
                    new TMapData.FindPathDataAllListenerCallback() {
                        @Override
                        public void onFindPathDataAll(Document document) {
                            parseDocument(document);
                        }
                    });

            // 직선 거리 계산
            TMapPolyLine line = new TMapPolyLine();
            line.addLinePoint(pathInfo.getStartPoint());
            line.addLinePoint(pathInfo.getEndPoint());
            pathInfo.setLineDistance(String.valueOf((int)line.getDistance()) + "m");
        }
    }

    private void parseDocument(Document document){
        Element root = document.getDocumentElement(); // 최상위 node

        Node time = root.getElementsByTagName(TAG_TOTAL_TIME).item(0); // 소요 시간
        pathInfo.setPathTime(DateUtil.getTime(time.getTextContent()));

        Node distance = root.getElementsByTagName(TAG_TOTAL_DISTANCE).item(0); // 총 거리
        pathInfo.setPathDistance(distance.getTextContent() + "m");
    }

    public void onFindPathClick(){
        navigator.startNaviActivity(pathInfo.getStartPoint(), pathInfo.getEndPoint());
    }
}
