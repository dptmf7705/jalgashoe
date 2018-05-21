package com.dankook.jalgashoe.sqliteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dankook.jalgashoe.Constant.SQL_CREATE_SEARCH_TABLE;
import static com.dankook.jalgashoe.Constant.SQL_DROP_SEARCH_TABLE;

/**
 * Created by yeseul on 2018-05-20.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_SEARCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_SEARCH_TABLE);
        onCreate(sqLiteDatabase);
    }
}
