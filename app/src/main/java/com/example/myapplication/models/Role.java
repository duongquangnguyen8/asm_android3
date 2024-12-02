package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("_id")
    private String id;
    private Number name;
    private String createdAt;
    private String updatedAt;

    public Role( Number name, String createdAt, String updatedAt) {
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Number getName() {
        return name;
    }

    public void setName(Number name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
