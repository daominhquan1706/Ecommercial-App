package com.example.test1706;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Add_productActivity extends AppCompatActivity  {
    public static final int PICK_IMAGE = 1;
    TextView tv_productname,tv_price;
    Spinner spinner_category;
    Button btn_chooseImg;
    private static final String TAG = "Add_productActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ArrayAdapter adapter= ArrayAdapter.createFromResource(this,R.array.category_nitewatch,R.layout.spinner_item_dark);

        tv_productname = (TextView) findViewById(R.id.tv_productname_add);
        tv_price= (TextView) findViewById(R.id.tv_product_price_add);
        spinner_category = (Spinner) findViewById(R.id.spinner_add);
        btn_chooseImg = (Button) findViewById(R.id.btn_choose_img);
        btn_chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
        }
        else{
            Toast.makeText(this, "đây là thông báo", Toast.LENGTH_SHORT).show();
        }
    }


    private void init(){

    }



}
