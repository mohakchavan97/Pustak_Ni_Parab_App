package com.mohakchavan.pustakniparab.Models;

public class Names {

    private long ser_no;
    private String FIRST_NAME, LAST_NAME, BLK_OR_FLT_NO, STREET_NAME, LOCALITY_OR_AREA, CONTACT;

    public Names() {
    }

    public Names(String FIRST_NAME, String LAST_NAME, String BLK_OR_FLT_NO, String STREET_NAME, String LOCALITY_OR_AREA, String CONTACT) {
        this.FIRST_NAME = FIRST_NAME;
        this.LAST_NAME = LAST_NAME;
        this.BLK_OR_FLT_NO = BLK_OR_FLT_NO;
        this.STREET_NAME = STREET_NAME;
        this.LOCALITY_OR_AREA = LOCALITY_OR_AREA;
        this.CONTACT = CONTACT;
    }

    public Names(int ser_no, String FIRST_NAME, String LAST_NAME, String BLK_OR_FLT_NO, String STREET_NAME, String LOCALITY_OR_AREA, String CONTACT) {
        this.ser_no = ser_no;
        this.FIRST_NAME = FIRST_NAME;
        this.LAST_NAME = LAST_NAME;
        this.BLK_OR_FLT_NO = BLK_OR_FLT_NO;
        this.STREET_NAME = STREET_NAME;
        this.LOCALITY_OR_AREA = LOCALITY_OR_AREA;
        this.CONTACT = CONTACT;
    }

    public void setSer_no(long ser_no) {
        this.ser_no = ser_no;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public void setBLK_OR_FLT_NO(String BLK_OR_FLT_NO) {
        this.BLK_OR_FLT_NO = BLK_OR_FLT_NO;
    }

    public void setSTREET_NAME(String STREET_NAME) {
        this.STREET_NAME = STREET_NAME;
    }

    public void setLOCALITY_OR_AREA(String LOCALITY_OR_AREA) {
        this.LOCALITY_OR_AREA = LOCALITY_OR_AREA;
    }

    public void setCONTACT(String CONTACT) {
        this.CONTACT = CONTACT;
    }

    public long getSer_no() {
        return ser_no;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public String getBLK_OR_FLT_NO() {
        return BLK_OR_FLT_NO;
    }

    public String getSTREET_NAME() {
        return STREET_NAME;
    }

    public String getLOCALITY_OR_AREA() {
        return LOCALITY_OR_AREA;
    }

    public String getCONTACT() {
        return CONTACT;
    }
}
