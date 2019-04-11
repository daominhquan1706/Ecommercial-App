package com.example.test1706.model;

import java.util.List;

public class Orders {
    public long creationTime;
    public double total;
    public String userID;
    public String customerName;
    public String customerAddress;
    public String customerPhoneNumber;
    public String status;
    public List<Cart> orderDetails;

    public Orders(long creationTime, double total, String userID, String customerName, String customerAddress, String customerPhoneNumber, String status, List<Cart> orderDetails) {
        this.creationTime = creationTime;
        this.total = total;
        this.userID = userID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public Orders() {
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Cart> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<Cart> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
