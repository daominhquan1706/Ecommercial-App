package com.example.adminr.model;

import java.util.Date;
import java.util.List;

public class Orders implements Comparable<Orders> {
    private long creationTime;
    private double total;
    private String userID;
    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String status;
    private List<Cart> orderDetails;
    private String paymentid;
    private Double Address_Lat;
    private Double Address_Lng;
    private List<String> Timeline;
    private String shiper_uid;
    private String shiper_email;

    public String getShiper_uid() {
        return shiper_uid;
    }

    public void setShiper_uid(String shiper_uid) {
        this.shiper_uid = shiper_uid;
    }

    public String getShiper_email() {
        return shiper_email;
    }

    public void setShiper_email(String shiper_email) {
        this.shiper_email = shiper_email;
    }

    public List<String> getTimeline() {
        return Timeline;
    }

    public void setTimeline(List<String> timeline) {
        Timeline = timeline;
    }

    public Double getAddress_Lat() {
        return Address_Lat;
    }

    public void setAddress_Lat(Double address_Lat) {
        Address_Lat = address_Lat;
    }

    public Double getAddress_Lng() {
        return Address_Lng;
    }

    public void setAddress_Lng(Double address_ng) {
        Address_Lng = address_ng;
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


    @Override
    public int compareTo(Orders o) {
        Date datetime1 = new Date();
        datetime1.setTime(getCreationTime());

        Date datetime2 = new Date();
        datetime2.setTime(o.getCreationTime());
        return datetime2.compareTo(datetime1);
    }

}
