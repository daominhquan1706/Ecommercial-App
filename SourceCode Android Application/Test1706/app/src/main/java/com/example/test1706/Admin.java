package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Admin extends AppCompatActivity {

    CardView cv_sanpham, cv_tinnhan, cv_doanhthu, cv_donhang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initAdmin();
        cv_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AdminSanPham_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        cv_tinnhan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Admin.this,Admin_Message_Activity.class);
                startActivity(intent1);
            }
        });
        cv_doanhthu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Admin.this,chart.class);
                startActivity(intent2);
            }
        });
        cv_donhang.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Admin.this,MapsActivity.class);
                startActivity(intent2);
            }
        });

    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initAdmin() {
        cv_doanhthu = (CardView) findViewById(R.id.cv_doanhthu);
        cv_donhang = (CardView) findViewById(R.id.cv_donhang);
        cv_sanpham = (CardView) findViewById(R.id.cv_sanpham);
        cv_tinnhan = (CardView) findViewById(R.id.cv_tinnhan);
    }
}
