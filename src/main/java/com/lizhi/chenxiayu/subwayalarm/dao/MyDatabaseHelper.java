package com.lizhi.chenxiayu.subwayalarm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenxiayu on 2017/6/8.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "SubwayAlarm.db";
    public static final String TABLE_NAME = "cellinfo";
    public static  MyDatabaseHelper database;
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,city varchar,linenumber varchar,subwaystation varchar,cell_id varchar,lac varchar,mcc varchar,mnc varchar,singalstrength varchar,temp1 varchar,temp2 varchar)";
    public static MyDatabaseHelper getInstances(Context context){
        if(database!=null){
            return database;
        }else {
            return new MyDatabaseHelper(context);
        }
    }

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop"+TABLE_NAME);
        onCreate(db);
    }

}
