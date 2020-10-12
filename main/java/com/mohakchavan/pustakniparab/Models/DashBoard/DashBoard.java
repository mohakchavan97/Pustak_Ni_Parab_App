package com.mohakchavan.pustakniparab.Models.DashBoard;

public class DashBoard {

    private boolean isSimple;
    private DashBoard_Data[] data;

    public DashBoard() {
    }

    public DashBoard(boolean isSimple, DashBoard_Data[] data) throws Exception {
        if (data.length > 2) {
            throw new Exception("Length of DashBoard_Data cannot be greater than 2.");
        } else {
            this.isSimple = isSimple;
            this.data = data;
        }
    }

    public boolean isSimple() {
        return isSimple;
    }

    public void setSimple(boolean simple) {
        isSimple = simple;
    }

    public DashBoard_Data[] getData() {
        return data;
    }

    public void setData(DashBoard_Data[] data) {
        this.data = data;
    }
}