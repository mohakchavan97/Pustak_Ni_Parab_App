package com.mohakchavan.pustakniparab.Models;

public class Names {

    private int ser_no;
    private String firstName, lastName, blockOrHouseNum, streetName, area;
    private long call;

    public void setSer_no(int ser_no) {
        this.ser_no = ser_no;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBlockOrHouseNum(String blockOrHouseNum) {
        this.blockOrHouseNum = blockOrHouseNum;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCall(long call) {
        this.call = call;
    }

    public Names() {
    }

    public Names(int ser_no, String firstName, String lastName, String blockOrHouseNum, String streetName, String area, long call) {
        this.ser_no = ser_no;
        this.firstName = firstName;
        this.lastName = lastName;
        this.blockOrHouseNum = blockOrHouseNum;
        this.streetName = streetName;
        this.area = area;
        this.call = call;
    }

    public int getSer_no() {
        return ser_no;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBlockOrHouseNum() {
        return blockOrHouseNum;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getArea() {
        return area;
    }

    public long getCall() {
        return call;
    }
}
