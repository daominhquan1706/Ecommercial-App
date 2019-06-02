package com.example.shiper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiper.Adapter.Adapter_HoaDon_item;
import com.example.shiper.model.Orders;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class NhanDonFragment extends Fragment {
    List<Orders> list;
    RecyclerView recyclerView;
    Adapter_HoaDon_item adapter;
    DatabaseReference myRef;
    List<String> mkey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        return inflater.inflate(R.layout.fragment_nhandon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        laydata();

    }

    private void init() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.rv_hoadon);
        myRef = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        mkey = new ArrayList<>();
        adapter = new Adapter_HoaDon_item(getContext(), list, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void laydata() {
        final String Status = "Chờ lấy hàng";
        myRef.child("Orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue(Orders.class).getStatus().equals(Status)) {
                    list.add(dataSnapshot.getValue(Orders.class));
                    mkey.add(dataSnapshot.getKey());
                    Timber.d("onDataChange: dataSnapshot1.getKey() : %s", dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (mkey.contains(dataSnapshot.getKey())) {
                    if (!Objects.requireNonNull(dataSnapshot.getValue(Orders.class)).getStatus().equals(Status)) {
                        list.remove(mkey.indexOf(dataSnapshot.getKey()));
                        mkey.remove(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (Objects.requireNonNull(dataSnapshot.getValue(Orders.class)).getStatus().equals(Status)) {
                        list.add(dataSnapshot.getValue(Orders.class));
                        mkey.add(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();
                    }
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
    }
}
