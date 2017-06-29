package com.lizhi.chenxiayu.subwayalarm.entity;

/**
 * Created by chenxiayu on 2017/6/8.
 */

public class CellInfoBean {
    private String city ; //所在城市
    private String linenumber ; //地铁线号
    private String subwaystation;//到站
    private int id;//数据id

    public String geteNodeBID() {
        return eNodeBID;
    }

    public void seteNodeBID(String eNodeBID) {
        this.eNodeBID = eNodeBID;
    }

    private String eNodeBID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(String linenumber) {
        this.linenumber = linenumber;
    }

    public String getSubwaystation() {
        return subwaystation;
    }

    public void setSubwaystation(String subwaystation) {
        this.subwaystation = subwaystation;
    }

    private String cell_id ;//连接基站编号
    private String lac ; //连接基站位置区域码
    private String mcc ;//国家码
    private String mnc ;//网号
    private String singalstrength;//连接基站信号强度

    public String getCell_id() {
        return cell_id;
    }

    public void setCell_id(String cell_id) {
        this.cell_id = cell_id;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getSingalstrength() {
        return singalstrength;
    }

    public void setSingalstrength(String singalstrength) {
        this.singalstrength = singalstrength;
    }
}
