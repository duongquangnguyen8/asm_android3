package com.example.myapplication.models;

import java.util.List;

public class BillDetailsWrapperList {
        BillDetail billDetails;
        Number totalPrice;
        String statusBill;

    public BillDetailsWrapperList(BillDetail billDetails, Number totalPrice, String statusBill) {
        this.billDetails = billDetails;
        this.totalPrice = totalPrice;
        this.statusBill = statusBill;
    }

    public BillDetail getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(BillDetail billDetails) {
        this.billDetails = billDetails;
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
}
