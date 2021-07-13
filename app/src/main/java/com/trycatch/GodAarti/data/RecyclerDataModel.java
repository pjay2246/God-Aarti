package com.trycatch.GodAarti.data;

public class RecyclerDataModel {

    String name;
    String description;
    int images;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImages() {
        return images;
    }

    public RecyclerDataModel(String name, String description, int images) {
        this.name = name;
        this.description = description;
        this.images = images;
    }
}
