package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.test1706.model.Cart;
import com.example.test1706.model.Orders;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHoaDon_Activity extends AppCompatActivity {
    Adapter_HoaDon_item adapter_hoaDon_item;
    ListView listView_order_admin;
    Adapter_HoaDon_item adapter;
    List<Orders> list;
    DatabaseReference myRef;
    private static final String TAG = "AdminHoaDon_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hoa_don_);
        init();
        myRef.child("Orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue(Orders.class));
                Log.d(TAG, "onDataChange: dataSnapshot1.getKey() : " + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
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


    }

    public void init() {
        myRef = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<Orders>();
        adapter = new Adapter_HoaDon_item(this, list);
        listView_order_admin = (ListView) findViewById(R.id.lv_order_admin);
        listView_order_admin.setAdapter(adapter);
    }

}

