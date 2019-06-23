package com.example.test1706;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test1706.Adapter.Cart_Recycle_Adapter_NiteWatch;
import com.example.test1706.SQLite.CartSqliteHelper;
import com.example.test1706.model.Orders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.CALL_PHONE;

public class Admin_HoaDon_Details_activity extends AppCompatActivity {
    TextView tv_total_price;
    CartSqliteHelper cartSqliteHelper;
    TextView name_order, address_order, sdt_order, tv_Xac_nhan;
    Cart_Recycle_Adapter_NiteWatch adapter;
    RecyclerView lv_checkout;
    FirebaseDatabase db;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    ActionBar actionBar;
    String paymentId;
    Orders orders;
    private static final String TAG = "AdminHoaDon_Details_act";
    RelativeLayout admin_details_hoadon_inputlayout_sdt_order;
    CardView btn_Xac_nhan, btn_TuChoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hoa_don__details_activity);
        //Slidr.attach(this);
        init();
        Bundle b = getIntent().getExtras();
        lv_checkout.setVisibility(View.VISIBLE);
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
                    adapter = new Cart_Recycle_Adapter_NiteWatch(Admin_HoaDon_Details_activity.this, orders.getOrderDetails(), R.layout.item_checkout_item);
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

                    setupbtn_Xac_nhan(orders.getStatus());
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
                i.setData(Uri.parse("tel:" + orders.getCustomerPhoneNumber()));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(i);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });


    }

    private void setupbtn_Xac_nhan(String status) {
        if (status.equals("Chờ xác nhận")) {
            tv_Xac_nhan.setText("Xác nhận");
            btn_TuChoi.setVisibility(View.VISIBLE);
            btn_TuChoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("Orders").child(paymentId).child("status").setValue("Admin hủy").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            });
            btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("Orders").child(paymentId).child("status").setValue("Chờ lấy hàng").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            });
        } else if (status.equals("Chờ lấy hàng")) {
            tv_Xac_nhan.setText("Lấy Hàng");
            btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("Orders").child(paymentId).child("status").setValue("Đang giao").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            });
        } else if (status.equals("Đang giao")) {
            tv_Xac_nhan.setText("Đã giao");
            btn_Xac_nhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child("Orders").child(paymentId).child("status").setValue("Đã giao").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            });
        } else {
            btn_Xac_nhan.setVisibility(View.GONE);
        }

    }


    public void init() {
        orders = new Orders();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        btn_TuChoi = (CardView) findViewById(R.id.btn_TuChoi);
        admin_details_hoadon_inputlayout_sdt_order = (RelativeLayout) findViewById(R.id.cv_phone_admin_order);
        lv_checkout = (RecyclerView) findViewById(R.id.admin_details_hoadon_lv_checkout);
        btn_Xac_nhan = (CardView) findViewById(R.id.btn_Xac_nhan);
        tv_Xac_nhan = (TextView) findViewById(R.id.tv_Xac_nhan);
        tv_total_price = (TextView) findViewById(R.id.admin_details_hoadon_tv_total_price);
        name_order = (TextView) findViewById(R.id.admin_details_hoadon_name_order);
        address_order = (TextView) findViewById(R.id.admin_details_hoadon_address_order);
        sdt_order = (TextView) findViewById(R.id.admin_details_hoadon_sdt_order);
    }

}
