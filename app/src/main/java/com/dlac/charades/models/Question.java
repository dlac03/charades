package com.dlac.charades.models;

/**
 * Created by dlac on 2017. 04. 30..
 */

public class Question {
    private String text;
    private int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int category) {
        this.categoryId = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Question(int categoryId, String text ) {
        this.categoryId = categoryId;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Kategória: " + getCategoryId() + " Kérdés: " + getText();
    }
}
