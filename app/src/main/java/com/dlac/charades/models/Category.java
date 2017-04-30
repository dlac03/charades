package com.dlac.charades.models;

/**
 * Created by dlac on 2017. 04. 30..
 */

public class Category {

    private String name;
    private int id;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " Név: " + getName();
    }
}
