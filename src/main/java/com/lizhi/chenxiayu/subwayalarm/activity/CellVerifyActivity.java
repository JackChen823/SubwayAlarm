package com.lizhi.chenxiayu.subwayalarm.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lizhi.chenxiayu.subwayalarm.R;
import com.lizhi.chenxiayu.subwayalarm.adapter.CellInfoListAdapter;
import com.lizhi.chenxiayu.subwayalarm.dao.CellInfodb;
import com.lizhi.chenxiayu.subwayalarm.entity.CellInfoBean;
import com.lizhi.chenxiayu.subwayalarm.service.AlarmReceiver;
import com.lizhi.chenxiayu.subwayalarm.utils.GetCellInfo;
import com.lizhi.chenxiayu.subwayalarm.utils.util;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by chenxiayu on 2017/6/12.
 */

public class CellVerifyActivity extends Activity implements View.OnClickListener{
    private Button infoRecoderButton;

    private Spinner city;
    private Spinner subwayStation;
    private Spinner lineNumber;
    private static final  String[] subwayStations ={"宋家庄","肖村","小红门","旧宫","亦庄桥","亦庄文化园","万源街","荣京东街","荣昌东街","同济南路","经海路","次渠南","次渠"};
    private static final String[] subwayStations_14 ={"善各庄","来广营","东湖渠","望京","阜通","望京南","将台","东风北桥","枣营","朝阳公园","金台路","大望路","九龙山","北工大西门","十里河","方庄","蒲黄榆","景泰","永定门外","北京南站"};
    private static final String[] subwayStations_10 ={"车道沟","长春桥","火器营","巴沟","苏州街","海淀黄庄","知春里","知春路","西土城","牡丹园","健德门","北土城","安贞门","惠新西街南口","芍药居","太阳宫","三元桥","亮马桥","农业展览馆","团结湖",
            "呼家楼","金台夕照","国贸","双井","劲松","潘家园","十里河","分钟寺","成寿寺","宋家庄","石榴庄","大红门","角门东","角门西","草桥","纪家庙","首经贸","丰台站","泥洼","西局","六里桥","莲花桥","公主坟","西钓鱼台","慈寿寺"};
    private static final String[] lineNumbers = new String[]{"亦庄线","14号线","10号线"};

    private ArrayAdapter<String> subwayStationAdapter ;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> lineNumberAdapter;
    private TextView subwayInfo;
    private TextView subwayInfoCurrent;
    private TextView getDataVerify;
    private ListView list;
    private CellInfoListAdapter cellInfoListAdapter;
    private CellInfodb db;
    private TextView subwayInfo1;
    private Button verifyFail;
    private Button verifyOK;
    private Cursor mResult ;
    private String verifyResult;
    private String eNodeBID;
    private TelephonyManager telephonyManager;
    private Calendar calendar;
    private AlarmManager am;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ArrayList <CellInfoBean> data = (ArrayList<CellInfoBean>) msg.obj;
                    BindListData(data);
                    break;
                case 2:
                    mResult = (Cursor) msg.obj;
                    if(mResult.moveToNext()){
                        int id = mResult.getInt(0);
                        String subStation = mResult.getString(3);
                        list.smoothScrollToPosition(list.getCount()-id);
                        if(am!=null&&isAlarm==false){
                            stopAlarm(am);
                            if(alertDialog!=null){
                                alertDialog.dismiss();
                            }
                            return;
                        }
                        if(isFinishing()&&alertDialog!=null){
                            alertDialog.dismiss();
                        }else if (isAlarm ==true){
                            if(alertDialog!=null){
                                alertDialog.dismiss();
                            }
                            showAlertDialog(subStation);
                            startAlarm();
                        }
                    }
                    break;
            }

        }
    };
    private Button reverseBt;
    private Boolean isReverse = false;

    private void stopAlarm(AlarmManager am) {
        Intent intent = new Intent(CellVerifyActivity.this,AlarmReceiver.class);
        intent.putExtra("music",false);
        sendBroadcast(intent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CellVerifyActivity.this,0,intent,0);
        am.cancel(pendingIntent);
    }

    private void startAlarm() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.i("chenxiayu","CellVerifyActivity---+startAlarm");
        Intent intent = new Intent(CellVerifyActivity.this, AlarmReceiver.class);
        intent.putExtra("music", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CellVerifyActivity.this, 0, intent, 0);
        //获取系统进程
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+(10*1000), pendingIntent);
    }

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    private void showAlertDialog(String subStation) {
        alertDialogBuilder = new AlertDialog.Builder(CellVerifyActivity.this);
        alertDialogBuilder.setTitle("到站提示");
        alertDialogBuilder.setMessage(subStation+"到了");
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String[] alarmCellInfo;
    private Boolean isAlarm = false;

    private void BindListData(ArrayList<CellInfoBean> data) {
        cellInfoListAdapter = new CellInfoListAdapter(getBaseContext(), data,verifyResult);
        list.setAdapter(cellInfoListAdapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
    }

    private void initView() {
        setContentView(R.layout.subway_info_verify);
        city = (AppCompatSpinner) findViewById(R.id.city);
        subwayStation = (AppCompatSpinner) findViewById(R.id.subway_station);
        lineNumber = (AppCompatSpinner) findViewById(R.id.line_number);
        getDataVerify = (TextView) findViewById(R.id.get_date_verify);
        getDataVerify.setText(util.getSystemTime()+"   Verify");
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        calendar = Calendar.getInstance();


        list = (ListView) findViewById(R.id.list);
        db = new CellInfodb();
        util.initData(getBaseContext() , db , handler);

        cityAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item,new String[]{"北京"});
        city.setAdapter(cityAdapter);

        lineNumberAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item,lineNumbers);
        lineNumber.setAdapter(lineNumberAdapter);
        lineNumber.setOnItemSelectedListener(new LineNumberSpinnerSelectedListener());

        GetCellInfo.getCellInfo(getBaseContext());

        subwayInfo = (TextView) findViewById(R.id.subway_info);
        subwayInfoCurrent = (TextView) findViewById(R.id.subway_info_current);

        reverseBt = (Button) findViewById(R.id.reversebt);
        reverseBt.setOnClickListener(this);


        if(GetCellInfo.beans.getCell_id()!=null){
            eNodeBID = util.convertCellID(Integer.parseInt(GetCellInfo.beans.getCell_id(),10));
            GetCellInfo.beans.seteNodeBID(eNodeBID);
        }
        subwayInfoCurrent.setText("Lac:"+GetCellInfo.beans.getLac()+" eNodeBID:"+GetCellInfo.beans.geteNodeBID()+" Cid:"+GetCellInfo.beans.getCell_id());

        verifyFail = (Button) findViewById(R.id.subway_info_fail);
        verifyFail.setOnClickListener(this);
        verifyOK = (Button) findViewById(R.id.subway_info_ok);
        verifyOK.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.subway_info_fail:
                if(!isAlarm){
                    isAlarm = true;
                    verifyFail.setText("取消闹钟");
                    telephonyManager.listen(new CellLocationChangedListner(getBaseContext(),alarmCellInfo[0],alarmCellInfo[1],alarmCellInfo[2],alarmCellInfo[3],alarmCellInfo[4]),PhoneStateListener.LISTEN_CELL_LOCATION);
                }else{
                    isAlarm = false;
                    if (am!=null&&isAlarm==false){
                        stopAlarm(am);
                    }
                    verifyFail.setText("设置闹钟");
                }

                break;
            case R.id.subway_info_ok:
                isAlarm = false;
                telephonyManager.listen(new CellLocationChangedListner(),PhoneStateListener.LISTEN_CELL_LOCATION);
                break;
            case R.id.reversebt:
                if(!isReverse){
                    Toast.makeText(getBaseContext(),"反向",Toast.LENGTH_LONG).show();
                    isReverse = true;
                    reverseBt.setText("正向");
                    lineNumber.setOnItemSelectedListener(new LineNumberSpinnerSelectedListener());
                }else {
                    isReverse = false;
                    reverseBt.setText("反向");
                    lineNumber.setOnItemSelectedListener(new LineNumberSpinnerSelectedListener());
                }
                break;

        }
    }



    private class SpinnerSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(GetCellInfo.beans.getLinenumber().equals(lineNumbers[0])){
                GetCellInfo.beans.setSubwaystation(subwayStations[position]);
            }else if(GetCellInfo.beans.getLinenumber().equals(lineNumbers[1])){
                GetCellInfo.beans.setSubwaystation(subwayStations_14[position]);
            }else{
                GetCellInfo.beans.setSubwaystation(subwayStations_10[position]);
            }
            Cursor result = db.queryByLineAndStation(getBaseContext(),GetCellInfo.beans.getSubwaystation(),GetCellInfo.beans.getLinenumber());
            if(result.moveToNext()){
                subwayInfo.setText("Lac:"+result.getString(5)+" eNodeBID:"+result.getString(9)+" Cid:"+result.getString(4));
                alarmCellInfo = new String[]{GetCellInfo.beans.getSubwaystation(),GetCellInfo.beans.getLinenumber(),result.getString(5),result.getString(9),result.getString(4)};
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataVerify.setText(util.getSystemTime()+"   Verify");
    }

    private class LineNumberSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            GetCellInfo.beans.setCity("北京");
            GetCellInfo.beans.setLinenumber(lineNumbers[position]);
            if(lineNumbers[position].equals(lineNumbers[0])){
                if(isReverse){
                    subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,util.swap(subwayStations));
                }else {
                    subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,subwayStations);
                }
            }else if(lineNumbers[position].equals(lineNumbers[1])){
                if(isReverse){
                    subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,util.swap(subwayStations_14));
                }else {
                    subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,subwayStations_14);
                }

            }else {
                if(isReverse){
                    subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,util.swap(subwayStations_10));
                }else {
                    subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,subwayStations_10);
                }

            }
            subwayStation.setAdapter(subwayStationAdapter);
            subwayStation.setOnItemSelectedListener(new SpinnerSelectedListener());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class CellLocationChangedListner extends PhoneStateListener {
        private String subStation;
        private String lineNumber;
        private String lac;
        private String meNodeBID;
        private String cellID;
        private Context mCotext;
        public CellLocationChangedListner() {
        }
        public CellLocationChangedListner(Context context,String subStation,String lineNumber, String lac ,String eNodeBID,String cellID) {
            this.subStation = subStation;
            this.lineNumber = lineNumber;
            this.lac = lac;
            this.meNodeBID = eNodeBID;
            this.cellID = cellID;
            this.mCotext = context;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Cursor result = db.queryBylacAndeNodeBID(getBaseContext(),GetCellInfo.beans.getLac(),GetCellInfo.beans.geteNodeBID());
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.obj = result;
                msg.sendToTarget();
            }
        };

        @Override
        public void onCellLocationChanged(CellLocation location) {
            if(location!=null){
                GsmCellLocation l1 = (GsmCellLocation) location;
                GetCellInfo.beans.setLac(l1.getLac()+"");
                GetCellInfo.beans.setCell_id(l1.getCid()+"");
                eNodeBID = util.convertCellID(l1.getCid());
                GetCellInfo.beans.seteNodeBID(eNodeBID);
                subwayInfoCurrent.setText("Lac:"+GetCellInfo.beans.getLac()+" eNodeBID:"+GetCellInfo.beans.geteNodeBID()+" Cid:"+GetCellInfo.beans.getCell_id());
                Log.i("chenxiayu","GetCellInfo.beans.getLac()---"+GetCellInfo.beans.getLac());
                Log.i("chenxiayu","lac---"+lac);
                //Toast.makeText(getBaseContext(),"beans.getLac().equals(lac)="+(GetCellInfo.beans.getLac().equals(lac)),Toast.LENGTH_LONG).show();
                if(isAlarm == true){
                    new Thread(runnable).start();
                }
            }
            else {
                Toast.makeText(getBaseContext(), "Location。。。空", Toast.LENGTH_LONG).show();
            }
        }
    }
}
