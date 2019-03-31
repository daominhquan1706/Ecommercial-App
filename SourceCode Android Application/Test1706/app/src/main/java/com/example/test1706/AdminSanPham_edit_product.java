package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.test1706.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSanPham_edit_product extends AppCompatActivity {
    EditText tv_productname, tv_price, tv_url_image, tv_category, tv_url_image_night, tv_quantity, tv_discount;
    Spinner spinner_category;
    Button btn_chooseImg, btn_chooseImg_Night, btn_add_product;
    ImageView img_choosen, img_chossen_night;
    private static final String TAG = "AdminSanPham_edit_product";
    DatabaseReference myRef;
    FirebaseDatabase database;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);


    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void init() {
        tv_url_image_night = (EditText) findViewById(R.id.tv_url_image_night);
        tv_category = (EditText) findViewById(R.id.tv_Category);
        tv_url_image = (EditText) findViewById(R.id.tv_url_image);
        tv_productname = (EditText) findViewById(R.id.tv_productname_add);
        tv_price = (EditText) findViewById(R.id.tv_product_price_add);
        spinner_category = (Spinner) findViewById(R.id.spinner_add);
        btn_chooseImg = (Button) findViewById(R.id.btn_choose_img);
        btn_chooseImg_Night = (Button) findViewById(R.id.btn_choose_img_night);
        img_choosen = (ImageView) findViewById(R.id.img_choose);
        img_chossen_night = (ImageView) findViewById(R.id.img_choose_night);
        btn_add_product = (Button) findViewById(R.id.btn_add_product);
        tv_quantity = (EditText) findViewById(R.id.tv_quantity);
        tv_discount = (EditText) findViewById(R.id.tv_discount);
    }
}
