package com.lizhi.chenxiayu.subwayalarm.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v7.widget.AppCompatSpinner;
import android.telephony.CellInfo;
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
import android.widget.Toast;

import com.lizhi.chenxiayu.subwayalarm.R;
import com.lizhi.chenxiayu.subwayalarm.adapter.CellInfoListAdapter;
import com.lizhi.chenxiayu.subwayalarm.dao.CellInfodb;
import com.lizhi.chenxiayu.subwayalarm.entity.CellInfoBean;
import com.lizhi.chenxiayu.subwayalarm.utils.GetCellInfo;
import com.lizhi.chenxiayu.subwayalarm.utils.util;

import java.util.ArrayList;

/**
 * Created by chenxiayu on 2017/6/8.
 */

public class CellInfoActivity extends Activity implements View.OnClickListener{

    private TextView subwayInfo;
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
    private TextView getDate;
    private ListView list;
    private CellInfodb db;
    private CellInfoListAdapter cellInfoListAdapter;
    private Cursor mResult;
    private TelephonyManager telephonyManager;
    private Handler handler = new Handler(){

        private ArrayList<CellInfoBean> data;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    data = (ArrayList<CellInfoBean>) msg.obj;
                    BindListData(data);
                    break;
                case 1:
                    mResult = (Cursor) msg.obj;
                    if(mResult.moveToNext()){
                        Log.i("chenxiayu","eNodeBID++"+GetCellInfo.beans.geteNodeBID());
                        db.update(getBaseContext(),GetCellInfo.beans);
                        util.initData(getBaseContext(),db ,handler);
                    }else {
                        db.inertCellInfo(getBaseContext(),GetCellInfo.beans);
                        util.initData(getBaseContext(),db ,handler);
                    }
                    break;
                case 2:
                    mResult = (Cursor) msg.obj;
                    if(mResult.moveToNext()){
                        int id = mResult.getInt(0);
                        //list.setSelection(id);
                        list.smoothScrollToPosition(list.getCount()-id);
                    }
            }
        }
    };
    private String eNodeBID;

    private void BindListData(ArrayList<CellInfoBean> data) {
        cellInfoListAdapter = new CellInfoListAdapter(getBaseContext(),data,null);
        list.setAdapter(cellInfoListAdapter);
    }

    private Button infoRefreshButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
    }

    private void initView() {
        setContentView(R.layout.subway_info_recorder);
        city = (AppCompatSpinner) findViewById(R.id.city);
        subwayStation = (AppCompatSpinner) findViewById(R.id.subway_station);
        lineNumber = (AppCompatSpinner) findViewById(R.id.line_number);
        getDate = (TextView) findViewById(R.id.get_date);
        getDate.setText(util.getSystemTime());
        list = (ListView) findViewById(R.id.list);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        db = new CellInfodb();
        util.initData(getBaseContext(),db ,handler);


        cityAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item,new String[]{"北京"});
        city.setAdapter(cityAdapter);

        lineNumberAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item,lineNumbers);
        lineNumber.setAdapter(lineNumberAdapter);
        lineNumber.setOnItemSelectedListener(new LineNumberSpinnerSelectedListener());



        subwayInfo = (TextView) findViewById(R.id.subway_info);
        infoRecoderButton = (Button) findViewById(R.id.info_recoder);
        infoRefreshButton = (Button) findViewById(R.id.info_refresh);
        infoRefreshButton.setOnClickListener(this);
        infoRecoderButton.setOnClickListener(this);
        GetCellInfo.getCellInfo(getBaseContext());

        //int i= Integer.parseInt(GetCellInfo.beans.getCell_id(),10);
        if(GetCellInfo.beans.getCell_id()!=null){
            eNodeBID = util.convertCellID(Integer.parseInt(GetCellInfo.beans.getCell_id(),10));
            GetCellInfo.beans.seteNodeBID(eNodeBID);
        }
        subwayInfo.setText("Lac:"+GetCellInfo.beans.getLac()+" eNodeBID:"+eNodeBID+" Cid:"+GetCellInfo.beans.getCell_id());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info_recoder:
                new Thread(){
                    @Override
                    public void run() {

                        //beans.setSubwaystation("宋家庄");
                        Cursor result = db.queryByLineAndStation(getBaseContext(),GetCellInfo.beans.getSubwaystation(),GetCellInfo.beans.getLinenumber());
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = result;
                        msg.sendToTarget();

                    }
                }.start();

                /*GetCellInfo.beans.setCity("北京");
                GetCellInfo.beans.setLinenumber("亦庄线");
                //beans.setSubwaystation("宋家庄");
                Cursor result = db.queryBySubwaystation(getBaseContext(),GetCellInfo.beans.getSubwaystation());
                if(result.moveToNext()){
                    Log.i("chenxiayu","update");
                    db.update(getBaseContext(),GetCellInfo.beans);
                    cellInfoListAdapter.notifyDataSetChanged();
                    db.close();
                }else {
                    Log.i("chenxiayu","insert");
                    db.inertCellInfo(getBaseContext(),GetCellInfo.beans);
                    cellInfoListAdapter.notifyDataSetChanged();
                    db.close();
                }*/
                break;
            case R.id.info_refresh:
                Toast.makeText(getBaseContext(),"刷新",Toast.LENGTH_LONG).show();
                telephonyManager.listen(new CellLocationChangedListner(), PhoneStateListener.LISTEN_CELL_LOCATION);
                subwayInfo.setText("Lac:"+GetCellInfo.beans.getLac()+" eNodeBID:"+GetCellInfo.beans.geteNodeBID()+" Cid:"+GetCellInfo.beans.getCell_id());
                break;
        }

    }


    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Cursor result = db.queryByLineAndStation(getBaseContext(),GetCellInfo.beans.getSubwaystation(),GetCellInfo.beans.getLinenumber());
                Message msg = handler.obtainMessage();
                msg.what = 2;
                msg.obj = result;
                msg.sendToTarget();
            }
        };

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(GetCellInfo.beans.getLinenumber().equals(lineNumbers[0])){
                Toast.makeText(getBaseContext(),subwayStations[position],Toast.LENGTH_LONG).show();
                GetCellInfo.beans.setSubwaystation(subwayStations[position]);
            }else if(GetCellInfo.beans.getLinenumber().equals(lineNumbers[1])){
                Toast.makeText(getBaseContext(),subwayStations_14[position],Toast.LENGTH_LONG).show();
                GetCellInfo.beans.setSubwaystation(subwayStations_14[position]);
            }else{
                Toast.makeText(getBaseContext(),subwayStations_10[position],Toast.LENGTH_LONG).show();
                GetCellInfo.beans.setSubwaystation(subwayStations_10[position]);
            }
            new Thread(runnable).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDate.setText(util.getSystemTime());
    }

    private class LineNumberSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            GetCellInfo.beans.setCity("北京");
            GetCellInfo.beans.setLinenumber(lineNumbers[position]);
            if(lineNumbers[position].equals(lineNumbers[0])){
                subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,subwayStations);
            }else if(lineNumbers[position].equals(lineNumbers[1])){
                subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,subwayStations_14);
            }else {
                subwayStationAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,subwayStations_10);
            }
            subwayStation.setAdapter(subwayStationAdapter);
            subwayStation.setOnItemSelectedListener(new SpinnerSelectedListener());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CellLocationChangedListner extends PhoneStateListener {
        @Override
        public void onCellLocationChanged(CellLocation location) {
            if(location!=null){
                GsmCellLocation l1 = (GsmCellLocation) location;
                GetCellInfo.beans.setLac(l1.getLac()+"");
                GetCellInfo.beans.setCell_id(l1.getCid()+"");
                eNodeBID = util.convertCellID(l1.getCid());
                GetCellInfo.beans.seteNodeBID(eNodeBID);

            }
            else {
                Toast.makeText(getBaseContext(), "Location。。。空", Toast.LENGTH_LONG).show();
            }
        }
    }
}
