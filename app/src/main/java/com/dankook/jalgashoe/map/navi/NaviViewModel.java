package com.dankook.jalgashoe.map.navi;

import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dankook.jalgashoe.R;
import com.dankook.jalgashoe.data.dao.BaseDao;
import com.dankook.jalgashoe.data.vo.PathInfoVO;
import com.dankook.jalgashoe.data.vo.PathLineVO;
import com.dankook.jalgashoe.data.vo.PathPointVO;
import com.dankook.jalgashoe.data.vo.PathVO;
import com.dankook.jalgashoe.util.BitmapUtil;
import com.dankook.jalgashoe.util.DateUtil;
import com.dankook.jalgashoe.util.XmlParserUtil;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.*;

/**
 * Created by yeseul on 2018-05-22.
 */

public class NaviViewModel {

    public final ObservableField<String> currentAddress = new ObservableField<>("현재 위치 찾는중 ...");

    private TMapView tMapView;
    private TMapGpsManager gpsManager;
    private TMapData tMapData;

    private NaviNavigator navigator;
    private PathInfoVO pathInfo;

    private List<PathVO> pathList = new ArrayList<>();

    public void setNavigator(NaviNavigator navigator) {
        this.navigator = navigator;
    }

    public void start(TMapView tMapView, TMapGpsManager gpsManager, PathInfoVO pathInfo) {
        this.tMapView = tMapView;
        this.gpsManager = gpsManager;
        this.pathInfo = pathInfo;
        this.tMapData = new TMapData();

        setupMapView();
        setupGpsManager();
        getPath();
    }

    private void setupMapView() {
        tMapView.setSKTMapApiKey("b7bfb971-b45e-40d9-8edb-8b2b46bfb04d"); // key 설정
        tMapView.setZoomLevel(19); // 지도 줌레벨 설정 (7~19)
        tMapView.setIconVisibility(true); // 현재위치 아이콘 나타내기
        tMapView.setMapType(TMapView.MAPTYPE_HYBRID); // 일반 지도 사용
        changeCurrentLocation(pathInfo.getStartPoint()); // 현재 위치를 출발점으로 변경

        tMapView.setCompassMode(false); // 나침반 모드 해제
        tMapView.setSightVisible(false); // 시야 표출 해제

//        tMapView.setIcon(BitmapUtil.getScaledBitmap(tMapView.getResources(), R.drawable.ic_navigation, 130)); // 현재위치 아이콘
        tMapView.setTrackingMode(true); // 사용자 위치 따라서 이동하는 모드로

        // 출발, 도착 아이콘 변경
        Bitmap startIcon = BitmapUtil.writeTextOnDrawable(tMapView.getResources(), R.drawable.ic_location_active, 130, "출발");
        Bitmap endIcon = BitmapUtil.writeTextOnDrawable(tMapView.getResources(), R.drawable.ic_marker_destination, 130, "도착");
        tMapView.setTMapPathIcon(startIcon, endIcon);
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

    private void changeCurrentAddress(double latitude, double longitude){
        tMapData.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                currentAddress.set(s);
            }
        });
    }

    public void changeCurrentLocation(TMapPoint location){
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude(), true); // 현재위치로 화면 이동
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 현재 위치 변경
        changeCurrentAddress(location.getLatitude(), location.getLongitude());
        tMapView.setRotate(tMapView.getRotate() - 45);
        tMapView.setMarkerRotate(true);
        tMapView.setPathRotate(true);
        tMapView.setPOIRotate(true);
    }

    private void getPath() {
        tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                pathInfo.getStartPoint(), pathInfo.getEndPoint(),
                new TMapData.FindPathDataAllListenerCallback() {
                    @Override
                    public void onFindPathDataAll(Document document) {
                        parseDocument(document);
                        drawLine();
                    }
                });
    }

    private void drawLine() {
        TMapPolyLine line = new TMapPolyLine();
        line.setLineWidth(30);
        line.setLineColor(tMapView.getResources().getColor(R.color.coral));

        for(PathVO path : pathList){
            List<TMapPoint> pointList = path.getPoints();
            for(TMapPoint point : pointList) {
                line.addLinePoint(point);
            }
        }
        tMapView.addTMapPath(line);
    }

    private void parseDocument(Document document){
        Element root = document.getDocumentElement(); // 최상위 node

        Node time = root.getElementsByTagName(TAG_TOTAL_TIME).item(0); // 소요 시간
        pathInfo.setPathTime(DateUtil.getTime(time.getTextContent()));

        Node distance = root.getElementsByTagName(TAG_TOTAL_DISTANCE).item(0); // 총 거리
        pathInfo.setPathDistance(distance.getTextContent() + "m");

        NodeList placeMarkList = root.getElementsByTagName(TAG_PLACEMARK); // 경로 정보

        for(int i = 0 ; i < placeMarkList.getLength(); i++){
            Node node = placeMarkList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){ // 노드 존재
                PathVO vo = parsePathInfo((Element) node); // 경로정보 파싱
                if(vo != null){
                    pathList.add(vo);
                }
            }
        }
    }

    private PathVO parsePathInfo(Element pathInfo){
        PathVO path;
        String nodeType = XmlParserUtil.getTagValue(TAG_NODE_TYPE, pathInfo); // line or point

        if(nodeType != null && nodeType.equals(NODE_TYPE_POINT)){ // point 정보 파싱
            PathPointVO point = new PathPointVO();
            point.setPointIndex(XmlParserUtil.getIntTagValue(TAG_POINT_INDEX, pathInfo));
            point.setTurnType(XmlParserUtil.getIntTagValue(TAG_TURN_TYPE, pathInfo));
            point.setPointType(XmlParserUtil.getTagValue(TAG_POINT_TYPE, pathInfo));
            point.setDirection(XmlParserUtil.getTagValue(TAG_DIRECTION, pathInfo));
            point.setNearPoiName(XmlParserUtil.getTagValue(TAG_NEAR_POI_NAME, pathInfo));
            point.setNearPoiPoint(new TMapPoint(
                    XmlParserUtil.getDoubleTagValue(TAG_NEAR_POI_LATITUDE, pathInfo),
                    XmlParserUtil.getDoubleTagValue(TAG_NEAR_POI_LONGITUDE, pathInfo)
                )
            );
            point.setIntersectName(XmlParserUtil.getTagValue(TAG_INTERSECTION_NAME, pathInfo));

            Node lineString = pathInfo.getElementsByTagName(TAG_POINT).item(0); // 경, 위도 정보
            if(lineString.getNodeType() == Node.ELEMENT_NODE){
                point.setPoints(parsePathPoint((Element) lineString));
            }
            path = point;
        } else if (nodeType != null && nodeType.equals(NODE_TYPE_LINE)) { // line 정보 파싱
            PathLineVO line = new PathLineVO();
            line.setLineIndex(XmlParserUtil.getIntTagValue(TAG_LINE_INDEX, pathInfo));
            line.setDistance(XmlParserUtil.getIntTagValue(TAG_LINE_DISTANCE, pathInfo));
            line.setTime(XmlParserUtil.getIntTagValue(TAG_LINE_TIME, pathInfo));
            line.setRoadType(XmlParserUtil.getIntTagValue(TAG_ROAD_TYPE, pathInfo));
            line.setCategoryRoadType(XmlParserUtil.getIntTagValue(TAG_CATEGORY_ROAD_TYPE, pathInfo));

            Node lineString = pathInfo.getElementsByTagName(TAG_LINESTRING).item(0); // 경, 위도 정보
            if(lineString.getNodeType() == Node.ELEMENT_NODE){
                line.setPoints(parsePathPoint((Element) lineString));
            }
            path = line;
        } else {
            return null;
        }

        // point, line 동일 정보 파싱
        path.setType(nodeType);
        path.setIndex(XmlParserUtil.getIntTagValue(TAG_PATH_INDEX, pathInfo)); // 경로 인덱스
        path.setName(XmlParserUtil.getTagValue(TAG_PATH_NAME, pathInfo)); // 지점, 도로 이름
        path.setDescription(XmlParserUtil.getTagValue(TAG_DESCRIPTION, pathInfo)); // 경로 설명
        path.setFacilityType(XmlParserUtil.getIntTagValue(TAG_FACILITY_TYPE, pathInfo)); // 시설물 타입
        path.setFacilityName(XmlParserUtil.getTagValue(TAG_FACILITY_NAME, pathInfo)); // 시설물 이름

        return path;
    }

    private List<TMapPoint> parsePathPoint(Element points){
        List<TMapPoint> pointList = new ArrayList<>();
        String value = XmlParserUtil.getTagValue(TAG_COORDINATES, points);
        if(value != null) {
            String[] sets = value.split(" "); // 경(lon), 위(lat)도 세트
            for (String set : sets) {
                if(set.equals("")) continue;
                String[] p = set.split(",");
                pointList.add(new TMapPoint(Double.parseDouble(p[1]), Double.parseDouble(p[0])));
            }
        }
        return pointList;
    }
}
