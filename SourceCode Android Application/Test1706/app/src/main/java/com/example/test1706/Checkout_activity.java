package com.example.test1706;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.Config.Config;
import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

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
    ListView_Adapter_checkout_item listView_adapter_checkout_item;
    MyListView lv_checkout;

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

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPayment();
            }
        });


        listView_adapter_checkout_item = new ListView_Adapter_checkout_item(this, cartSqliteHelper.getAllCarts());
        lv_checkout.setAdapter(listView_adapter_checkout_item);
    }

    public void init() {
        lv_checkout = (MyListView) findViewById(R.id.lv_checkout);
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

                        for (Cart item : cartSqliteHelper.getAllCarts()) {
                            cartSqliteHelper.deleteCart(item);
                        }
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, Checkout_PaymentDetails_activity.class)
                                .putExtra("Checkout_PaymentDetails_activity", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }
}
