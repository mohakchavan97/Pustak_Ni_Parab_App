package com.mohakchavan.pustakniparab.Models.DashBoard;

public class DashBoard_Data {
    private double topData;
    private String bottomData;

    public DashBoard_Data() {
    }

    public DashBoard_Data(double topData, String bottomData) {
        this.topData = topData;
        this.bottomData = bottomData;
    }

    public double getTopData() {
        return topData;
    }

    public void setTopData(double topData) {
        this.topData = topData;
    }

    public String getBottomData() {
        return bottomData;
    }

    public void setBottomData(String bottomData) {
        this.bottomData = bottomData;
    }
}
