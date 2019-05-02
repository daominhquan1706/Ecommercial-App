package com.example.test1706.model;

public class UserAccount {
    public String email;
    public String name;
    public String sdt;
    public String uid;
    public String diachi;

    public UserAccount(String email, String name, String sdt, String uid, String diachi) {
        this.email = email;
        this.name = name;
        this.sdt = sdt;
        this.uid = uid;
        this.diachi = diachi;
    }

    public UserAccount() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
