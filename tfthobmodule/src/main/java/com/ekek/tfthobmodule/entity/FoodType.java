package com.ekek.tfthobmodule.entity;

public class FoodType {

    private int imageResourceID;
    private String name;

    public FoodType(int imageResourceID, String name) {
        this.imageResourceID = imageResourceID;
        this.name = name;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
