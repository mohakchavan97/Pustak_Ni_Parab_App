package com.mohakchavan.pustakniparab.Models.DashBoard;

import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;

public class DashBoard_Data {
    private long topData;
    private String bottomData;

    private BaseSeries<DataPoint> graphData;

    public DashBoard_Data() {
    }

    public DashBoard_Data(long topData, String bottomData) {
        this.topData = topData;
        this.bottomData = bottomData;
    }

    public DashBoard_Data(String bottomData, BaseSeries<DataPoint> graphData) {
        this.bottomData = bottomData;
        this.graphData = graphData;
    }

    public BaseSeries<DataPoint> getGraphData() {
        return graphData;
    }

    public void setGraphData(BaseSeries<DataPoint> graphData) {
        this.graphData = graphData;
    }

    public long getTopData() {
        return topData;
    }

    public void setTopData(long topData) {
        this.topData = topData;
    }

    public String getBottomData() {
        return bottomData;
    }

    public void setBottomData(String bottomData) {
        this.bottomData = bottomData;
    }
}
