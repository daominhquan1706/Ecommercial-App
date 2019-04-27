package com.example.test1706;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.test1706.Config.Config;
import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Orders;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Checkout_activity extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 7171;
    TextView tv_total_price;
    CartSqliteHelper cartSqliteHelper;
    EditText name_order, address_order, sdt_order;
    ImageView btnPaynow;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // su dung sandbox de test
            .clientId(Config.PAYPAL_CLIENT_ID);
    String amount = "";
    Cart_Recycle_Adapter_NiteWatch cart_recycle_adapter_niteWatch;
    RecyclerView lv_checkout;
    FirebaseDatabase db;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_activity);
        init();
        tv_total_price.setText(String.valueOf(cartSqliteHelper.getCartPriceCount()));
        // bat dau severvice paypaly
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Glide.with(this).load(R.drawable.ba_cai_dong_ho).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    collapsingToolbarLayout.setBackground(resource);
                }
            }
        });
        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_order.getText().toString().equals("")) {
                    name_order.setError("Name is required");
                    name_order.requestFocus();
                } else if (sdt_order.getText().toString().equals("")) {
                    sdt_order.setError("Phone Number is required");
                    sdt_order.requestFocus();
                } else if (address_order.getText().toString().equals("")) {
                    address_order.setError("Address is required");
                    address_order.requestFocus();
                } else {
                    ProcessPayment();
                }
            }
        });


        listView_adapter_checkout_item = new ListView_Adapter_checkout_item(this, cartSqliteHelper.getAllCarts());
        lv_checkout.setAdapter(listView_adapter_checkout_item);

        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.btnPaynow), "Hướng dẫn sử dụng", "Click để dùng tài khoản PayPal thanh toán")
                        .tintTarget(false)
                        .outerCircleColor(R.color.MoneyColor));
    }

    public void init() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        lv_checkout = (RecyclerView) findViewById(R.id.lv_checkout);
        cartSqliteHelper = new CartSqliteHelper(this);
        btnPaynow = (ImageView) findViewById(R.id.btnPaynow);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        name_order = (EditText) findViewById(R.id.name_order);
        address_order = (EditText) findViewById(R.id.address_order);
        sdt_order = (EditText) findViewById(R.id.sdt_order);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(Checkout_activity.this, PayPalService.class));
        super.onDestroy();
    }

    private void ProcessPayment() {
        amount = tv_total_price.getText().toString();
        PayPalPayment PaypalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Thanh toan san pham", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(Checkout_activity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PaypalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        AddCart_to_Order(paymentDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    public void AddCart_to_Order(String paymentDetails) {
        try {
            JSONObject jsonObject = new JSONObject(paymentDetails);
            JSONObject response = jsonObject.getJSONObject("response");
            String id = response.getString("id");
            Orders orders = new Orders();
            orders.setCreationTime(System.currentTimeMillis());
            orders.setCustomerAddress(address_order.getText().toString());
            orders.setCustomerName(name_order.getText().toString());
            orders.setCustomerPhoneNumber(sdt_order.getText().toString());
            orders.setStatus("Chờ xác nhận");
            orders.setTotal(cartSqliteHelper.getCartPriceCount());
            orders.setPaymentid(id);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                orders.setUserID(currentUser.getUid());
            }
            List<Cart> orderDetails = new ArrayList<Cart>();
            for (Cart item : cartSqliteHelper.getAllCarts()) {
                orderDetails.add(item);
                cartSqliteHelper.deleteCart(item);
            }
            orders.setOrderDetails(orderDetails);

            myRef.child("Orders").child(id).setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(Checkout_activity.this, Checkout_PaymentDetails_activity.class)
                            .putExtra("Checkout_PaymentDetails_activity", paymentDetails)
                            .putExtra("PaymentAmount", amount)
                    );
                    finish();


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
