package com.example.test1706;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test1706.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import android.view.ViewGroup.LayoutParams;

public class MainFragment extends Fragment {
    DatabaseReference myRef;
    ProductAdapter productadapter;
    ProgressBar progressBar;
    ListView listView;
    int listheight=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        //SLIDE SHOW
        ViewPager v_page_massage = (ViewPager) getView().findViewById(R.id.v_pager_fragment_message);
        ImageAdapter adapter = new ImageAdapter(getActivity());
        v_page_massage.setAdapter(adapter);
        //v_page_massage.setVisibility(View.GONE);

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);


        final List<Product> productList = new ArrayList<Product>();
        final List<String> mkey = new ArrayList<String>();
        productadapter = new ProductAdapter(getActivity(), productList);
        listView = (ListView) getView().findViewById(R.id.listView_product);
        listView.setAdapter(productadapter);
        //DATABASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        //getProductdata();


        // Read from the database
        myRef.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product itemproduct = dataSnapshot.getValue(Product.class);
                productList.add(itemproduct);
                productadapter.notifyDataSetChanged();
                mkey.add(dataSnapshot.getKey());
                Log.d("dữ liệu Firebase", "đã lấy thành công đồng hồ " + itemproduct.getProduct_Name());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                productList.set(mkey.indexOf(dataSnapshot.getKey()), dataSnapshot.getValue(Product.class));
                productadapter.notifyDataSetChanged();
                Log.d("UPDATE dữ liệu ", dataSnapshot.getValue(Product.class).getProduct_Name() + s);
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
        dialog.dismiss();
    }

    private void getProductdata() {
        List<Product> list_data = new ArrayList<Product>();
        Product dongho1 = new Product("Đồng hồ sồ 1", 300, "Sport", "dongho_1");
        Product dongho2 = new Product("Đồng hồ sồ 2", 300, "Fashion", "dongho_2");
        Product dongho3 = new Product("Đồng hồ sồ 3", 300, "Bussiness", "dongho_3");
        myRef.push().setValue(dongho1);
        myRef.push().setValue(dongho2);
        myRef.push().setValue(dongho3);
    }
}
