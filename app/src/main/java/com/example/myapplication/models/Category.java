package com.example.myapplication.models;

import android.media.Image;
import android.widget.ImageView;

public class Category {
    private int image;
    private String type;

    public Category(){};
    public Category(int image, String type) {
        this.image = image;
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
