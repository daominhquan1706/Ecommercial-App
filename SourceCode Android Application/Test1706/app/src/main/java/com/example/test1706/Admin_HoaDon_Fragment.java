package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.test1706.model.Orders;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Admin_HoaDon_Fragment extends Fragment {
    Adapter_HoaDon_item adapter_hoaDon_item;
    ListView listView_order_admin;
    Adapter_HoaDon_item adapter;
    List<Orders> list;
    DatabaseReference myRef;
    private static final String TAG = "Admin_HoaDon_Fragment";
    String Status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Status = bundle.getString("Status", ("Nothing"));
        }
        return inflater.inflate(R.layout.fragment_admin__hoa_don, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        myRef.child("Orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (Objects.requireNonNull(dataSnapshot.getValue(Orders.class)).getStatus().equals(Status)) {
                    list.add(dataSnapshot.getValue(Orders.class));
                    Log.d(TAG, "onDataChange: dataSnapshot1.getKey() : " + dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                }


                if (list.size() == 0) {
                    listView_order_admin.setVisibility(View.INVISIBLE);
                }
                else{
                    listView_order_admin.setVisibility(View.VISIBLE);
                }


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
        adapter = new Adapter_HoaDon_item(getActivity(), list);
        listView_order_admin = (ListView) getView().findViewById(R.id.lv_order_admin);
        listView_order_admin.setAdapter(adapter);
    }

}

