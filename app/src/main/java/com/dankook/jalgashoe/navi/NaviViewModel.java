package com.dankook.jalgashoe.navi;

import android.databinding.ObservableField;
import android.graphics.Color;

import com.dankook.jalgashoe.data.vo.NavigationVO;
import com.dankook.jalgashoe.data.vo.PathBundleVO;
import com.dankook.jalgashoe.data.vo.PathInfoVO;
import com.dankook.jalgashoe.data.vo.PathLineVO;
import com.dankook.jalgashoe.data.vo.PathPointVO;
import com.dankook.jalgashoe.data.vo.PathVO;
import com.dankook.jalgashoe.util.DateUtil;
import com.dankook.jalgashoe.util.MapUtil;
import com.dankook.jalgashoe.util.XmlParserUtil;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.NODE_TYPE_LINE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.NODE_TYPE_POINT;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_CATEGORY_ROAD_TYPE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_COORDINATES;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_DESCRIPTION;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_DIRECTION;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_FACILITY_NAME;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_FACILITY_TYPE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_INTERSECTION_NAME;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_LINESTRING;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_LINE_DISTANCE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_LINE_INDEX;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_LINE_TIME;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_NEAR_POI_LATITUDE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_NEAR_POI_LONGITUDE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_NEAR_POI_NAME;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_NODE_TYPE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_PATH_INDEX;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_PATH_NAME;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_PLACEMARK;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_POINT;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_POINT_INDEX;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_POINT_TYPE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_ROAD_TYPE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_TOTAL_DISTANCE;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_TOTAL_TIME;
import static com.dankook.jalgashoe.Constant.PathInfoResponseParam.TAG_TURN_TYPE;

/**
 * Created by yeseul on 2018-05-22.
 */

public class NaviViewModel {

    public final ObservableField<String> currentAddress = new ObservableField<>("현재 위치 찾는중 ...");

    private TMapView tMapView;
    private TMapData tMapData = new TMapData();

    private NaviNavigator navigator;

    private PathInfoVO pathInfo;
    private NavigationVO naviInfo = new NavigationVO();

    private List<PathVO> pathList = new ArrayList<>();
    private List<PathPointVO> pointList = new ArrayList<>();
    private List<PathBundleVO> nextPathList = new ArrayList<>();
    private PathBundleVO currentPoint;

    public void setNavigator(NaviNavigator navigator) {
        this.navigator = navigator;
    }

    public NavigationVO getNaviInfo() {
        return naviInfo;
    }

    public void start(TMapView tMapView, PathInfoVO pathInfo) {
        this.tMapView = tMapView;
        this.pathInfo = pathInfo;

        changeCurrentLocation(pathInfo.getStartPoint()); // 현재 위치를 출발점으로 변경
        getPath();
    }

    public void changeCurrentLocation(TMapPoint location){
        MapUtil.changeCurrentLocation(tMapView, location);
        MapUtil.changeCurrentAddress(currentAddress, location);
    }

    private void getPath() {
        tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                pathInfo.getStartPoint(), pathInfo.getEndPoint(),
                new TMapData.FindPathDataAllListenerCallback() {
                    @Override
                    public void onFindPathDataAll(Document document) {
                        parseDocument(document);
                        startNavigation();
                    }
                });
    }

    // pathList에 저장된 경로를 탐색한다
    private void startNavigation() {
        drawLine();
        getPointList();

        navigator.startNavigation();
        changeToNextPoint();
    }

    private void changeToNextPoint(){
        do{
            currentPoint = nextPathList.remove(0);
        } while(currentPoint.getLineList().size() == 0);

        naviInfo.setTurnType(currentPoint.getPoint().getTurnType()); // 회전 정보
        naviInfo.setTurnName(currentPoint.getPoint().getIntersectName()); // 교차로 이름

        int distance = 0;
        for(PathLineVO line : currentPoint.getLineList()){
            distance += line.getDistance();
        }
        naviInfo.setTurnDistance(distance); // 교차로까지 거리
    }

    private void drawLine() {
        TMapPolyLine line = new TMapPolyLine();
        line.setLineWidth(10);
        line.setLineColor(Color.BLACK);

        for(PathVO path : pathList){
            List<TMapPoint> pointList = path.getPoints();
            for(TMapPoint point : pointList) {
                line.addLinePoint(point);
            }
        }
        tMapView.addTMapPath(line);
    }

    private void getPointList() {
        int lineCount = 0;

        for(int i = 0 ; i < pathList.size() ; i++){
            PathVO path = pathList.get(i);

            if(path instanceof PathLineVO){
                lineCount++;
            }

            if(path instanceof PathPointVO){
                PathBundleVO bundle = new PathBundleVO();
                bundle.setPoint((PathPointVO) path);
                for(int j = i - lineCount ; j < i ; j++){
                    if(pathList.get(j) instanceof PathLineVO){
                        bundle.addLineList((PathLineVO) pathList.get(j));
                    }
                }
                nextPathList.add(bundle);
                lineCount = 0;
            }
        }
    }

    public void calculateCurrentDistance(TMapPoint location) {
        tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                location, currentPoint.getPoint().getPoints().get(0),
                new TMapData.FindPathDataAllListenerCallback() {
                    @Override
                    public void onFindPathDataAll(Document document) {
                        Element root = document.getDocumentElement(); // 최상위 node

                        String dist = root.getElementsByTagName(TAG_TOTAL_DISTANCE).item(0).getTextContent(); // 총 거리
                        int distance = Integer.parseInt(dist);
                        naviInfo.setTurnDistance(distance);
                        processDistance(distance);
                    }
                });
    }

    private void processDistance(int distance) {
        if(distance < 5){
            navigator.showSnackBar("point 도착");
            changeToNextPoint();
        }
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
