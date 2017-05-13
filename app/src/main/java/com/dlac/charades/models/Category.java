package com.dlac.charades.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dlac on 2017. 04. 30..
 */

public class Category implements Parcelable {

    private String name;
    private String description;
    private int id;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.description = "";
    }

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    protected Category(Parcel in) {
        name = in.readString();
        description = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " Név: " + getName();
    }
}
