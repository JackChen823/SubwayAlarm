package com.lizhi.chenxiayu.subwayalarm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lizhi.chenxiayu.subwayalarm.entity.CellInfoBean;

/**
 * Created by chenxiayu on 2017/6/9.
 */
/*_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,city varchar,linenumber varchar" +
        "subwaystation varchar,cell_id varchar,lac varchar,mcc varchar,mnc varchar,singalstrength varchar)";*/
public class CellInfodb {

    private SQLiteDatabase database;

    //打开数据库，让数据库可读写
    public void openDB(Context context){
        MyDatabaseHelper helper = MyDatabaseHelper.getInstances(context);
        database = helper.getWritableDatabase();
    }
    //插入元数据
    public void inertCellInfo(Context context, CellInfoBean cellInfo){
        openDB(context);
        ContentValues cv  = new ContentValues();
        cv.put("city",cellInfo.getCity());
        cv.put("linenumber",cellInfo.getLinenumber());
        cv.put("subwaystation",cellInfo.getSubwaystation());
        cv.put("cell_id",cellInfo.getCell_id());
        cv.put("lac",cellInfo.getLac());
        cv.put("mcc",cellInfo.getMcc());
        cv.put("mnc",cellInfo.getMnc());
        cv.put("singalstrength",cellInfo.getSingalstrength());
        cv.put("temp1",cellInfo.geteNodeBID());
        database.insert(MyDatabaseHelper.TABLE_NAME,null,cv);
        database.close();
    }

    //查询所有数据
    public Cursor query(Context context){
        openDB(context);
        Cursor result = database.query(MyDatabaseHelper.TABLE_NAME,null,null,null,null,null,"_id DESC");
        return result;
    }
    //条件查询数据，以便判断更新使用
    public Cursor queryBySubwaystation(Context context , String subwayStation){
        openDB(context);
        String where = "subwaystation = ? ";
        String[] whereArgs = {subwayStation};
        Cursor result  =  database.query(MyDatabaseHelper.TABLE_NAME,null,where,whereArgs,null,null,null);
        return  result;
    }
    //根据站名和线路条件查询数据，以便判断更新使用。
    public Cursor queryByLineAndStation(Context context , String subwayStation,String lineNumber){
        openDB(context);
        String where = "subwaystation = ? and linenumber = ? ";
        String[] whereArgs = {subwayStation,lineNumber};
        Cursor result  =  database.query(MyDatabaseHelper.TABLE_NAME,null,where,whereArgs,null,null,null);
        return  result;
    }

    //根据lac和eNodeBID条件查询数据，以便判断使用。
    public Cursor queryBylacAndeNodeBID(Context context , String lac,String eNodeBID){
        openDB(context);
        String where = "lac = ? and temp1 = ? ";
        String[] whereArgs = {lac,eNodeBID};
        Cursor result  =  database.query(MyDatabaseHelper.TABLE_NAME,null,where,whereArgs,null,null,null);
        return  result;
    }

    //更新数据
    public void update(Context context , CellInfoBean cellInfo ){
        openDB(context);
        String where = "linenumber = ? and subwaystation = ? ";
        String[] whereArgs = {cellInfo.getLinenumber(),cellInfo.getSubwaystation()};
        ContentValues values = new ContentValues();
        values.put("linenumber",cellInfo.getLinenumber());
        values.put("subwaystation",cellInfo.getSubwaystation());
        values.put("cell_id",cellInfo.getCell_id());
        values.put("lac",cellInfo.getLac());
        values.put("mcc",cellInfo.getMcc());
        values.put("mnc",cellInfo.getMnc());
        values.put("singalstrength",cellInfo.getSingalstrength());
        values.put("temp1",cellInfo.geteNodeBID());
        database.update(MyDatabaseHelper.TABLE_NAME,values,where,whereArgs);
        database.close();
    }
    //删除数据
    public void delete(Context context ,CellInfoBean cellInfo) {
        openDB(context);
        //当条件满足id = 传入的参数的时候,就删除那整行数据,有可能有好几行都满足这个条件,满足的全部都删除
        String where = "id = ?";
        String[] whereArgs = {""};
        database.delete(MyDatabaseHelper.TABLE_NAME, where, whereArgs);
        database.close();
    }

    public void  close(){
        database.close();
    }
}
