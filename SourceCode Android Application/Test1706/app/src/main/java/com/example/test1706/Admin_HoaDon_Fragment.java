package com.example.test1706;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    RecyclerView listView_order_admin;
    Adapter_HoaDon_item adapter;
    List<Orders> list;
    DatabaseReference myRef;
    private static final String TAG = "Admin_HoaDon_Fragment";
    String Status;
    TextView tv_status_empty;
    List<String> mkey;
    RelativeLayout image_empty_hoadon;

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

        // Set up the login form.


        myRef.child("Orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (Objects.requireNonNull(dataSnapshot.getValue(Orders.class)).getStatus().equals(Status)) {
                    list.add(dataSnapshot.getValue(Orders.class));
                    mkey.add(dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
                if (list.size() == 0) {
                    listView_order_admin.setVisibility(View.INVISIBLE);
                } else {
                    image_empty_hoadon.setVisibility(View.GONE);
                    listView_order_admin.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (mkey.contains(dataSnapshot.getKey())) {
                    if (!Objects.requireNonNull(dataSnapshot.getValue(Orders.class)).getStatus().equals(Status)) {
                        list.remove(mkey.indexOf(dataSnapshot.getKey()));
                        mkey.remove(mkey.indexOf(dataSnapshot.getKey()));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (Objects.requireNonNull(dataSnapshot.getValue(Orders.class)).getStatus().equals(Status)) {
                        list.add(dataSnapshot.getValue(Orders.class));
                        mkey.add(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }


                if (list.size() == 0) {
                    listView_order_admin.setVisibility(View.INVISIBLE);
                } else {
                    listView_order_admin.setVisibility(View.VISIBLE);
                }
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

        tv_status_empty.setText(Status);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void init() {
        image_empty_hoadon = (RelativeLayout) getView().findViewById(R.id.image_empty_hoadon);
        mkey = new ArrayList<String>();
        tv_status_empty = (TextView) getView().findViewById(R.id.tv_status_empty);
        myRef = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<Orders>();
        adapter = new Adapter_HoaDon_item(getActivity(), list, getActivity());
        listView_order_admin = (RecyclerView) getView().findViewById(R.id.lv_order_admin);
        listView_order_admin.setAdapter(adapter);
    }

}

