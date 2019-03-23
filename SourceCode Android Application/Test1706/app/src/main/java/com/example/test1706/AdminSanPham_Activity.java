package com.example.test1706;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminSanPham_Activity extends AppCompatActivity {

    Button btn_addproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sanpham);

        btn_addproduct = (Button) findViewById(R.id.btn_add_product);
        btn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(AdminSanPham_Activity.this,Add_productActivity.class);
                startActivity(i);
            }
        });

    }
}
