package com.example.adminr.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AccountUser {
    private String UID;
    private String Email;
    private String Name;
    private String SDT;
    private String Diachi;
    private Double Lat_Location;
    private Double Long_Location;

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

    private static final String TAG = "AccountUser";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<AccountUser> accountUserList;

    public AccountUser(String UID, String email, String name, String SDT, String diachi) {
        this.UID = UID;
        Email = email;
        Name = name;
        this.SDT = SDT;
        Diachi = diachi;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }

    public AccountUser() {
    }

    public AccountUser(String UID, String email) {
        this.UID = UID;
        Email = email;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }


    public void update_firebaseAccount() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Account");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            AccountUser accountUser = new AccountUser(currentUser.getUid(), currentUser.getEmail());
            databaseReference.child(currentUser.getUid()).setValue(accountUser);
        }
    }
}
