package com.mohakchavan.pustakniparab.Models;

import java.util.ArrayList;
import java.util.List;

public class BaseData {

    private List<Issues> issuesList;
    private List<Names> namesList;
    private List<NewBooks> newBooksList;

    public BaseData() {
        issuesList = new ArrayList<>();
        namesList = new ArrayList<>();
        newBooksList = new ArrayList<>();
    }

    public BaseData(List<Issues> issuesList, List<Names> namesList, List<NewBooks> newBooksList) {
        this.issuesList = issuesList;
        this.namesList = namesList;
        this.newBooksList = newBooksList;
    }

    public List<Issues> getIssuesList() {
        return issuesList;
    }

    public void setIssuesList(List<Issues> issuesList) {
        this.issuesList = issuesList;
    }

    public List<Names> getNamesList() {
        return namesList;
    }

    public void setNamesList(List<Names> namesList) {
        this.namesList = namesList;
    }

    public List<NewBooks> getNewBooksList() {
        return newBooksList;
    }

    public void setNewBooksList(List<NewBooks> newBooksList) {
        this.newBooksList = newBooksList;
    }

    public void addIssue(Issues issue) {
        issuesList.add(issue);
    }

    public void addName(Names name) {
        namesList.add(name);
    }

    public void addBook(NewBooks book) {
        newBooksList.add(book);
    }

    public long getTotalNewBooks() {
        long newBooks = 0;
        for (NewBooks books : newBooksList) {
            newBooks += Double.parseDouble(books.getTotalBooks());
        }
        return newBooks;
    }
}
