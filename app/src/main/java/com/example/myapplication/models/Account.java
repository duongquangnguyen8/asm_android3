package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Account {
    @SerializedName("_id")
    private String id;
    private String email;
    private String pass;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Date birth;
    private String roleId;
    private String createdAt;
    private String updateAt;

    public Account(){}
    public Account(String email, String pass, String fullName, String address, String phoneNumber, Date birth, String roleId, String createdAt, String updateAt) {
        this.email = email;
        this.pass = pass;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
