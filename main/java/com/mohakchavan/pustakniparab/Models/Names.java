package com.mohakchavan.pustakniparab.Models;

public class Names {

    private long ser_no;
    private String firstName, lastName, blkOrFltNo, streetName, localityOrArea, contact;

    public Names() {
    }

    public Names(String firstName, String lastName, String blkOrFltNo, String streetName, String localityOrArea, String contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.blkOrFltNo = blkOrFltNo;
        this.streetName = streetName;
        this.localityOrArea = localityOrArea;
        this.contact = contact;
    }

    public Names(int ser_no, String firstName, String lastName, String blkOrFltNo, String streetName, String localityOrArea, String contact) {
        this.ser_no = ser_no;
        this.firstName = firstName;
        this.lastName = lastName;
        this.blkOrFltNo = blkOrFltNo;
        this.streetName = streetName;
        this.localityOrArea = localityOrArea;
        this.contact = contact;
    }

    public String returnFullName() {
        String text = (!firstName.isEmpty()) ? firstName : "";
        text += (!lastName.isEmpty()) ? " " + lastName : "";
        return text;
    }

    public String returnFullAddress() {
        String text = (!blkOrFltNo.isEmpty()) ? blkOrFltNo.concat(", ") : "";
        text += (!streetName.isEmpty()) ? streetName.concat(", ") : "";
        text += (!localityOrArea.isEmpty()) ? localityOrArea : "";
        return text;
    }

    public void setSer_no(long ser_no) {
        this.ser_no = ser_no;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBlkOrFltNo(String blkOrFltNo) {
        this.blkOrFltNo = blkOrFltNo;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setLocalityOrArea(String localityOrArea) {
        this.localityOrArea = localityOrArea;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public long getSer_no() {
        return ser_no;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBlkOrFltNo() {
        return blkOrFltNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getLocalityOrArea() {
        return localityOrArea;
    }

    public String getContact() {
        return contact;
    }
}
