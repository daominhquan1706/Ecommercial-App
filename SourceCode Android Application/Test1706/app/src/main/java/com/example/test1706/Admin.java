package com.example.test1706;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class Admin extends AppCompatActivity {

    CardView cv_sanpham, cv_tinnhan, cv_doanhthu, cv_donhang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_activity);

        initAdmin ();
        cv_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this,AdminSanPham_Activity.class);
                startActivity(intent);
            }
        });

    }
    
    private void initAdmin(){
        cv_doanhthu = (CardView) findViewById(R.id.cv_doanhthu);
        cv_donhang= (CardView) findViewById(R.id.cv_donhang);
        cv_sanpham= (CardView) findViewById(R.id.cv_sanpham);
        cv_tinnhan= (CardView) findViewById(R.id.cv_tinnhan);
    }
}
