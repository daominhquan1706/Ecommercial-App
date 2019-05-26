package com.example.shiper.Model;

import java.util.List;

public class AccountUser {
    private String UID;
    private String Email;
    private Double Lat_Location;
    private Double Long_Location;
    private List<String> DonHangDaGiao;
    private String Status;

    public AccountUser(String UID, String email, Double lat_Location, Double long_Location, List<String> donHangDaGiao, String status) {
        this.UID = UID;
        Email = email;
        Lat_Location = lat_Location;
        Long_Location = long_Location;
        DonHangDaGiao = donHangDaGiao;
        Status = status;
    }

    public AccountUser() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public Double getLat_Location() {
        return Lat_Location;
    }

    public void setLat_Location(Double lat_Location) {
        Lat_Location = lat_Location;
    }

    public Double getLong_Location() {
        return Long_Location;
    }

    public void setLong_Location(Double long_Location) {
        Long_Location = long_Location;
    }

    public List<String> getDonHangDaGiao() {
        return DonHangDaGiao;
    }

    public void setDonHangDaGiao(List<String> donHangDaGiao) {
        DonHangDaGiao = donHangDaGiao;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
