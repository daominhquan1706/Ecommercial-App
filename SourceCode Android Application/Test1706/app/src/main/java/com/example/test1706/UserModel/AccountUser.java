package com.example.test1706.UserModel;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AccountUser {
    private String UID;
    private String Email;
    private String Name;
    private String SDT;
    private String Diachi;
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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Account");
            accountUserList = new ArrayList<AccountUser>();

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            AccountUser accountUser = new AccountUser(currentUser.getUid(), currentUser.getEmail(), "", "", "");

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    accountUserList.add(dataSnapshot.getValue(AccountUser.class));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            boolean isExist = false;

            for (AccountUser item : accountUserList) {
                if (item.getUID().equals(currentUser.getUid())) {
                    isExist = true;
                }
            }

            if (!isExist) {
                databaseReference.child(currentUser.getUid()).setValue(accountUser);
            }
        }


    }

    public AccountUser getCurrentUser() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUID = firebaseUser.getUid();
        final boolean[] issucess = {false};
        final AccountUser[] accountUser = {new AccountUser()};
        if (firebaseUser != null) {
            databaseReference.child("Account").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    accountUser[0] = dataSnapshot.getValue(AccountUser.class);
                    issucess[0] =true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        while(!issucess[0]){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return accountUser[0];
    }
}
