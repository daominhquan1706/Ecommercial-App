package com.example.test1706.model;

import java.util.List;

public class Orders {
    private long creationTime;
    private double total;
    private String userID;
    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String status;
    private List<Cart> orderDetails;
    private String paymentid;
    private String Address_Lat;
    private String Address_ng;
    private List<String> Timeline;

    public List<String> getTimeline() {
        return Timeline;
    }

    public void setTimeline(List<String> timeline) {
        Timeline = timeline;
    }

    public String getAddress_Lat() {
        return Address_Lat;
    }

    public void setAddress_Lat(String address_Lat) {
        Address_Lat = address_Lat;
    }

    public String getAddress_ng() {
        return Address_ng;
    }

    public void setAddress_ng(String address_ng) {
        Address_ng = address_ng;
    }

    public Orders() {
    }

    public Orders(long creationTime, double total, String userID, String customerName, String customerAddress, String customerPhoneNumber, String status, List<Cart> orderDetails, String paymentid) {
        this.creationTime = creationTime;
        this.total = total;
        this.userID = userID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.status = status;
        this.orderDetails = orderDetails;
        this.paymentid = paymentid;
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

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }
}
