package com.example.adminr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;

import org.json.JSONException;
import org.json.JSONObject;

public class Checkout_PaymentDetails_activity extends AppCompatActivity {

    TextView
            txtId,
            txtAmount,
            txtStatus,
            tvtsCreationTime,
            tv_nguoinhan,
            tv_sodienthoai,
            tv_diachi;
    Button btn_continue_shopping, btn_xemHoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        //Slidr.attach(this);

        tv_nguoinhan = (TextView) findViewById(R.id.tv_nguoinhan);
        tv_sodienthoai = (TextView) findViewById(R.id.tv_sodienthoai);
        tv_diachi = (TextView) findViewById(R.id.tv_diachi);


        txtId = (TextView) findViewById(R.id.txtId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtStatus = (TextView) findViewById(R.id.txtsStatus);
        tvtsCreationTime = (TextView) findViewById(R.id.tvtsCreationTime);
        btn_continue_shopping = (Button) findViewById(R.id.btn_continue_shopping);
        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("Checkout_PaymentDetails_activity"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        btn_continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Checkout_PaymentDetails_activity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        tv_nguoinhan.setText(intent.getStringExtra("tennguoinhan"));
        tv_sodienthoai.setText(intent.getStringExtra("sodienthoai"));
        tv_diachi.setText(intent.getStringExtra("diachi"));


        btn_xemHoaDon = (Button) findViewById(R.id.btn_xemHoaDon);
        btn_xemHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Checkout_PaymentDetails_activity.this, User_HoaDon_Activity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Checkout_PaymentDetails_activity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText("Chờ xác nhận");
            tvtsCreationTime.setText(response.getString("create_time"));
            txtAmount.setText(String.valueOf("$" + paymentAmount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
