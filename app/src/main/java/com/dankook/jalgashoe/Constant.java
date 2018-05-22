package com.dankook.jalgashoe;

/**
 * Created by yeseul on 2018-05-20.
 */

public class Constant {
    public static final String DATABASE_NAME_SEARCH = "search_history.db";

    public static final String TABLE_NAME_SEARCH = "search";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TEXT = "text";
    public static final String COLUMN_NAME_DATE = "date";

    public static final String SQL_CREATE_SEARCH_TABLE
            = "create table search(id integer primary key autoincrement, text text, date text);";

    public static final String SQL_DROP_SEARCH_TABLE
            = "drop table search;";

    public static final String SQL_SELECT_SEARCH_LIST
            = "select * from search;";

    public static final String SQL_DELETE_SEARCH_ITEM_BY_ID
            = "delete from search where id=";

    public static final String SQL_INSERT_INTO_SEARCH
            = "insert into search(text, date) ";

    public static class PathInfoResponseParam {
        public static final String TAG_TOTAL_DISTANCE = "tmap:totalDistance"; // 총 거리(m)
        public static final String TAG_TOTAL_TIME = "tmap:totalTime"; // 예상 소요시간(초)
        public static final String TAG_PLACEMARK = "Placemark"; // 경로정보

        public static final String TAG_NODE_TYPE = "tmap:nodeType"; // 노드 정보 Point or Line
        public static final String NODE_TYPE_POINT = "POINT"; // point
        public static final String NODE_TYPE_LINE = "LINE"; // line

        // path 정보 구분 POINT or LINE
        public static final String TAG_PATH_INDEX = "tmap:index"; // path index
        public static final String TAG_POINT_INDEX = "tmap:pointIndex"; // point index
        public static final String TAG_LINE_INDEX = "tmap:lineIndex"; // point index

        // path 전체 정보
        public static final String TAG_PATH_NAME = "name"; // 지점 또는 도로 이름
        public static final String TAG_DESCRIPTION = "description"; // 길 안내 설명
        public static final String TAG_POINT = "Point"; // 지점 경위도
        public static final String TAG_LINESTRING = "LineString"; // 라인 경위도
        public static final String TAG_COORDINATES = "coordinates"; // 경, 위도 정보
        public static final String TAG_FACILITY_TYPE = "tmap:facilityType"; // 시설물 정보
        public static final String TAG_FACILITY_NAME = "tmap:facilityName"; // 시설물 이름

        // path point 정보
        public static final String TAG_TURN_TYPE = "tmap:turnType"; // 회전 정보
        public static final String TAG_POINT_TYPE = "tmap:pointType"; // 안내 지점 구분
        public static final String TAG_DIRECTION = "tmap:direction"; // 방면 안내
        public static final String TAG_NEAR_POI_NAME = "tmap:nearPoiName"; // 기준 poi 명칭
        public static final String TAG_NEAR_POI_LATITUDE = "tmap:nearPoiX"; // 기준 poi 경도
        public static final String TAG_NEAR_POI_LONGITUDE = "tmap:nearPoiY"; // 기준 poi 위도
        public static final String TAG_INTERSECTION_NAME = "tmap:intersectionName"; // 교차로 명칭

        // path line 정보
        public static final String TAG_LINE_DISTANCE = "tmap:distance"; // 구간 거리
        public static final String TAG_LINE_TIME = "tmap:time"; // 구간 시간
        public static final String TAG_ROAD_TYPE = "tmap:roadType"; // 도로 타입
        public static final String TAG_CATEGORY_ROAD_TYPE = "tmap:categoryRoadType"; // 특화거리 타입
    }
}
