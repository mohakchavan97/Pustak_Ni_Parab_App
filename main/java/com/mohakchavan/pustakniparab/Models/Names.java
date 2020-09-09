package com.mohakchavan.pustakniparab.Models;

public class Names {

    private int ser_no;
    private String fname, lname, blk, strt, area;
    private long call;

    public void setSer_no(int ser_no) {
        this.ser_no = ser_no;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setBlk(String blk) {
        this.blk = blk;
    }

    public void setStrt(String strt) {
        this.strt = strt;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCall(long call) {
        this.call = call;
    }

    public Names() {
    }

    public Names(int ser_no, String fname, String lname, String blk, String strt, String area, long call) {
        this.ser_no = ser_no;
        this.fname = fname;
        this.lname = lname;
        this.blk = blk;
        this.strt = strt;
        this.area = area;
        this.call = call;
    }

    public int getSer_no() {
        return ser_no;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getBlk() {
        return blk;
    }

    public String getStrt() {
        return strt;
    }

    public String getArea() {
        return area;
    }

    public long getCall() {
        return call;
    }
}
