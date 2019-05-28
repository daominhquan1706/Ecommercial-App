package com.example.test1706.model;

public class Timeline {
    String noidung;
    long thoigian;

    public Timeline(String noidung, long thoigian) {
        this.noidung = noidung;
        this.thoigian = thoigian;
    }

    public Timeline() {
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public long getThoigian() {
        return thoigian;
    }

    public void setThoigian(long thoigian) {
        this.thoigian = thoigian;
    }
}
