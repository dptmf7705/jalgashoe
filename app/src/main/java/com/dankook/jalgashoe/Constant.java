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


    public class ResponseParam {
        public static final String TAG_TATAL_DISTANCE = "tmap:totaldistance"; // 총 거리(m)
        public static final String TAG_TATAL_TIME = "tmap:totaltime"; // 예상 소요시간(초)
        public static final String TAG_PLACEMARK = "Placemark"; // 경로정보

        public static final String TAG_NODE_TYPE = "tmap:nodetype"; // 노드 정보 Point or Line
        public static final String NODE_TYPE_POINT = "POINT"; // point
        public static final String NODE_TYPE_LINE = "LINE"; // line

        public static final String NODE_
    }
}
