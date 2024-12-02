package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

public class Bill {
    @SerializedName("_id")
    private String id;
    private String accountId;
    private String cartId;
    private Number totalPrice;
    private String statusBill;
    private String shippingName;
    private String shippingAddress;
    private String shippingPhone;
    private String createdAt;
    private String updateAt;

    public Bill(String accountId, String cartId, Number totalPrice, String statusBill, String shippingName, String shippingAddress, String shippingPhone) {
        this.accountId = accountId;
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.statusBill = statusBill;
        this.shippingName = shippingName;
        this.shippingAddress = shippingAddress;
        this.shippingPhone = shippingPhone;
    }

    public Bill(String statusBill) {
        this.statusBill = statusBill;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public Number getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Number totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatusBill() {
        return statusBill;
    }

    public void setStatusBill(String statusBill) {
        this.statusBill = statusBill;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
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
