package com.mohakchavan.pustakniparab.Models;

public class NewBooks {

    private long newBookId;
    private String personName, totalBooks, language, registeredDate;

    public NewBooks(long newBookId, String personName, String totalBooks, String language, String registeredDate) {
        this.newBookId = newBookId;
        this.personName = personName;
        this.totalBooks = totalBooks;
        this.language = language;
        this.registeredDate = registeredDate;
    }

    public NewBooks(String personName, String totalBooks, String language, String registeredDate) {
        this.personName = personName;
        this.totalBooks = totalBooks;
        this.language = language;
        this.registeredDate = registeredDate;
    }

    public long getNewBookId() {
        return newBookId;
    }

    public void setNewBookId(long newBookId) {
        this.newBookId = newBookId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(String totalBooks) {
        this.totalBooks = totalBooks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
