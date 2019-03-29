package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.test1706.model.Product;
import com.example.test1706.model.RandomString;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminSanPham_Activity extends AppCompatActivity {

    Button btn_addproduct;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<Product> productList;
    List<String> mkey;
    Admin_Product_Recycle_Adapter_NiteWatch productadapter;
    RecyclerView listView_admin_product_nitewatch;
    private static final String TAG = "AdminSanPham_Activity";
LinearLayout layout_product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sanpham);

        init();
        btn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminSanPham_Activity.this, Add_productActivity.class);
                startActivity(i);
            }
        });
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        productList = new ArrayList<Product>();
        mkey = new ArrayList<String>();

        productadapter = new Admin_Product_Recycle_Adapter_NiteWatch(this, productList, 0);
        listView_admin_product_nitewatch.setAdapter(productadapter);


        Log.d(TAG, "onCreate: " + RandomString.alphanum);
        myRef.child("NiteWatch").child("HAWK").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Product itemproduct = item.getValue(Product.class);
                    productList.add(itemproduct);
                    productadapter.notifyDataSetChanged();
                    mkey.add(item.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        layout_product.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void init() {
        layout_product= (LinearLayout) findViewById(R.id.layout_product);
        btn_addproduct = (Button) findViewById(R.id.btn_add_product);
        listView_admin_product_nitewatch = (RecyclerView) findViewById(R.id.listView_admin_product_nitewatch);
    }
}
