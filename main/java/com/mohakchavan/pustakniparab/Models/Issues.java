package com.mohakchavan.pustakniparab.Models;

public class Issues {

    private long issueNo;
    private String bookName, bookPrice, bookAuthPub, issuerId, issuerName, issuerAddr, issuerCont, issueDate, isReturned, retDate;
    private boolean isChecked;

    public Issues() {
    }

    public Issues(long issueNo, String bookName, String bookPrice, String bookAuthPub, String issuerId, String issuerName, String issuerAddr, String issuerCont, String issueDate, String isReturned, String retDate) {
        this.issueNo = issueNo;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.bookAuthPub = bookAuthPub;
        this.issuerId = issuerId;
        this.issuerName = issuerName;
        this.issuerAddr = issuerAddr;
        this.issuerCont = issuerCont;
        this.issueDate = issueDate;
        this.isReturned = isReturned;
        this.retDate = retDate;
        this.isChecked = false;
    }

    public Issues(String bookName, String bookPrice, String bookAuthPub, String issuerId, String issuerName, String issuerAddr, String issuerCont, String issueDate, String isReturned, String retDate) {
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.bookAuthPub = bookAuthPub;
        this.issuerId = issuerId;
        this.issuerName = issuerName;
        this.issuerAddr = issuerAddr;
        this.issuerCont = issuerCont;
        this.issueDate = issueDate;
        this.isReturned = isReturned;
        this.retDate = retDate;
        this.isChecked = false;
    }

    public long getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(long issueNo) {
        this.issueNo = issueNo;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookAuthPub() {
        return bookAuthPub;
    }

    public void setBookAuthPub(String bookAuthPub) {
        this.bookAuthPub = bookAuthPub;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIssuerAddr() {
        return issuerAddr;
    }

    public void setIssuerAddr(String issuerAddr) {
        this.issuerAddr = issuerAddr;
    }

    public String getIssuerCont() {
        return issuerCont;
    }

    public void setIssuerCont(String issuerCont) {
        this.issuerCont = issuerCont;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getRetDate() {
        return retDate;
    }

    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    public String getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(String isReturned) {
        this.isReturned = isReturned;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
