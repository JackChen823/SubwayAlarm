package com.lizhi.chenxiayu.subwayalarm.utils;

import android.content.Context;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.lizhi.chenxiayu.subwayalarm.entity.CellInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiayu on 2017/6/12.
 */

public class GetCellInfo {
    public static List<CellInfoBean> list;
    public static CellInfoBean beans;

    public static  void getCellInfo(Context context) {
        // lac连接基站位置区域码 cellid连接基站编码 mcc MCC国家码 mnc MNC网号
        // signalstrength连接基站信号强度
        list = new ArrayList<CellInfoBean>();
        beans = new CellInfoBean();
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getNetworkOperator();
        // beans.setMcc(operator.substring(0, 3));
        //beans.setMnc(operator.substring(3));
        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {// 这是电信的
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager
                    .getCellLocation();
            beans.setCell_id(cdmaCellLocation.getBaseStationId() + "");
            beans.setLac(cdmaCellLocation.getNetworkId() + "");
        } else {// 这是移动和联通的
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager
                    .getCellLocation();
            if(gsmCellLocation!=null){
                beans.setCell_id(gsmCellLocation.getCid() + "");
                beans.setLac(gsmCellLocation.getLac() + "");
            }
        }
        beans.setSingalstrength("0");
        list.add(beans);
        List<CellInfo> infoLists = telephonyManager.getAllCellInfo();
        if (infoLists!=null&&infoLists.size() != 0) {
            for (CellInfo info : infoLists) {
                /** 1、GSM是通用的移动联通电信2G的基站。
                 2、CDMA是3G的基站。
                 3、LTE，则证明支持4G的基站。*/
                CellInfoBean bean = new CellInfoBean();
                if (info.toString().contains("CellInfoLte")) {
                    CellInfoLte cellInfoLte = (CellInfoLte) info;
                    CellIdentityLte cellIdentityLte = cellInfoLte
                            .getCellIdentity();
                    CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte
                            .getCellSignalStrength();
                    bean.setSingalstrength(cellSignalStrengthLte.getDbm() + "");
                    bean.setCell_id(cellIdentityLte.getCi() + "");
                    bean.setLac(cellIdentityLte.getTac() + "");
                    bean.setMcc(cellIdentityLte.getMcc() + "");
                    bean.setMnc(cellIdentityLte.getMnc() + "");
                } else if (info.toString().contains("CellInfoGsm")) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                    CellIdentityGsm cellIdentityGsm = cellInfoGsm
                            .getCellIdentity();
                    CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm
                            .getCellSignalStrength();
                    bean.setSingalstrength(cellSignalStrengthGsm.getDbm() + "");
                    bean.setCell_id(cellIdentityGsm.getCid() + "");
                    bean.setLac(cellIdentityGsm.getLac() + "");
                    bean.setMcc(cellIdentityGsm.getMcc() + "");
                    bean.setMnc(cellIdentityGsm.getMnc() + "");
                } else if (info.toString().contains("CellInfoCdma")) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
                    CellIdentityCdma cellIdentityCdma = cellInfoCdma
                            .getCellIdentity();
                    CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma
                            .getCellSignalStrength();
                    bean.setCell_id(cellIdentityCdma.getBasestationId() + "");
                    bean.setSingalstrength(cellSignalStrengthCdma.getCdmaDbm()
                            + "");
                    /**因为我要把这个list转成gson，所以这个对象的所有属性我都赋一下值，不必理会这里*/
                    /*bean.setLac("0");
                    bean.setMcc("0");
                    bean.setMnc("0");*/
                }
                list.add(bean);
            }
        }
    }

}
