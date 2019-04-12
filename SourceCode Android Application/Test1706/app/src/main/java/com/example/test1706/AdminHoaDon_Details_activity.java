package com.example.test1706;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Orders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.RequiresApi;

import static android.Manifest.permission.CALL_PHONE;

public class AdminHoaDon_Details_activity extends AppCompatActivity {
    TextView tv_total_price;
    CartSqliteHelper cartSqliteHelper;
    TextView name_order, address_order, sdt_order;
    ImageView btnPaynow;
    ListView_Adapter_checkout_item adapter;
    MyListView lv_checkout;
    FirebaseDatabase db;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    ActionBar actionBar;
    String paymentId;
    Orders orders;
    private static final String TAG = "AdminHoaDon_Details_act";
    CardView admin_details_hoadon_inputlayout_sdt_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hoa_don__details_activity);

        init();
        Bundle b = getIntent().getExtras();

        if (b != null) {
            paymentId = b.getString("PaymentId");
        } else {
            paymentId = "không truyền được tham số";
        }


        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Orders").child(paymentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders = dataSnapshot.getValue(Orders.class);

                if (orders != null) {
                    adapter = new ListView_Adapter_checkout_item(AdminHoaDon_Details_activity.this, orders.getOrderDetails());
                    lv_checkout.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    tv_total_price.setText(String.valueOf(orders.getTotal()));
                    name_order.setText(orders.getCustomerName());
                    address_order.setText(orders.getCustomerAddress());
                    sdt_order.setText(orders.getCustomerPhoneNumber());

                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setTitle(paymentId);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        admin_details_hoadon_inputlayout_sdt_order.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+orders.getCustomerPhoneNumber()));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });
    }

    public void init() {
        orders = new Orders();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        admin_details_hoadon_inputlayout_sdt_order = (CardView) findViewById(R.id.cv_phone_admin_order);
        lv_checkout = (MyListView) findViewById(R.id.admin_details_hoadon_lv_checkout);
        btnPaynow = (ImageView) findViewById(R.id.admin_details_hoadon_btnPaynow);
        tv_total_price = (TextView) findViewById(R.id.admin_details_hoadon_tv_total_price);
        name_order = (TextView) findViewById(R.id.admin_details_hoadon_name_order);
        address_order = (TextView) findViewById(R.id.admin_details_hoadon_address_order);
        sdt_order = (TextView) findViewById(R.id.admin_details_hoadon_sdt_order);
    }

}
