package com.dlac.charades.models;

/**
 * Created by dlac on 2017. 04. 30..
 */

public class Question {
    private String text;
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Question(Category category, String text ) {
        this.category = category;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Kategória: " + getCategory() + " Kérdés: " + getText();
    }
}
