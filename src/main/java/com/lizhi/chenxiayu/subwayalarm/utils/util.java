package com.lizhi.chenxiayu.subwayalarm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.lizhi.chenxiayu.subwayalarm.dao.CellInfodb;
import com.lizhi.chenxiayu.subwayalarm.entity.CellInfoBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenxiayu on 2017/6/12.
 */

public class util {
    private static ArrayList<CellInfoBean> cellInfoList ;
    public static String getSystemTime(){
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm");
        Date curDate =  new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    public static void initData(final Context context , final CellInfodb db  , final Handler handler) {
        cellInfoList = new ArrayList<CellInfoBean>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Cursor result = db.query(context);
                while (result.moveToNext()) {
                    CellInfoBean bean = new CellInfoBean();
                    String city = result.getString(1);
                    String linenumber = result.getString(2);
                    String subwaystation = result.getString(3);
                    String cell_id = result.getString(4);
                    String lac = result.getString(5);
                    String eNodeBID = result.getString(9);
                    bean.setCity(city);
                    bean.setLinenumber(linenumber);
                    bean.setSubwaystation(subwaystation);
                    bean.setCell_id(cell_id);
                    bean.setLac(lac);
                    bean.seteNodeBID(eNodeBID);
                    cellInfoList.add(bean);
                }
                Message msg = new Message();
                msg.what = 0;
                msg.obj =cellInfoList;
                handler.sendMessage(msg);
                db.close();
            }
        };
        new Thread(runnable).start();
    }
    /*
    检查是否存在SDcard
     */
    public static  boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else {
            return false;
        }
    }
    /*
    获取版本号，返回当前应用的版本号
     */
    public static int getVersion(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            String version = packageInfo.versionName;
            int versioncode = packageInfo.versionCode;
            return versioncode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /*
    转换成2进制，又移8位
     */
    public static String convertCellID(int cellNum) {
        Integer.toBinaryString(cellNum);
        cellNum = cellNum >> 8;
        return cellNum+"";
    }
    /*
    校验电话号码格式
     */
    public static Boolean  isPhoneNumberModel(String tle) {
        Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
        Matcher matcher = p.matcher(tle);
        return matcher.matches();
    }
    /*
    反转数组
     */
    public static String[] swap(String array[]){
        int len = array.length;
        for(int i=0;i<len/2;i++){
            String tmp = array[i];
            array[i] = array[len-1-i];
            array[len-1-i] = tmp;
        }
        return array;
    }
}
