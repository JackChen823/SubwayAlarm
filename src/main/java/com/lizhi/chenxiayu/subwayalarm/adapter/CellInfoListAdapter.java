package com.lizhi.chenxiayu.subwayalarm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lizhi.chenxiayu.subwayalarm.R;
import com.lizhi.chenxiayu.subwayalarm.dao.CellInfodb;
import com.lizhi.chenxiayu.subwayalarm.entity.CellInfoBean;
import com.lizhi.chenxiayu.subwayalarm.utils.GetCellInfo;
import com.lizhi.chenxiayu.subwayalarm.utils.util;

import java.util.ArrayList;

/**
 * Created by chenxiayu on 2017/6/12.
 */

public class CellInfoListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CellInfoBean> data;
    private ArrayList<CellInfoBean> list ;
    private TextView result;
    private String mResult;
    private ViewHolder viewHolder;

    public CellInfoListAdapter(Context context , ArrayList<CellInfoBean> data , String reslut) {
        this.context = context;
        this.data = data;
        this.mResult = reslut;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cellinfo_item,null);
            viewHolder = new ViewHolder();
            viewHolder.lineNumberItem = (TextView) convertView.findViewById(R.id.line_number_item);
            viewHolder.subwayStationItem = (TextView) convertView.findViewById(R.id.subway_station_item);
            viewHolder.laccid = (TextView) convertView.findViewById(R.id.lac_cid);
            //viewHolder.result = (TextView) convertView.findViewById(R.id.result);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lineNumberItem.setText(data.get(position).getLinenumber());
        viewHolder.subwayStationItem.setText(data.get(position).getSubwaystation());
        viewHolder.laccid.setText(data.get(position).getLac()+"--"+data.get(position).geteNodeBID()+"--"+data.get(position).getCell_id());
        /*if(viewHolder.result!= null){
            viewHolder.result.setVisibility(View.VISIBLE);
            viewHolder.result.setText(mResult);
        }*/
        return convertView;
    }

    private class ViewHolder {
        TextView lineNumberItem;
        TextView subwayStationItem;
        TextView laccid;
        TextView result;
    }
}
