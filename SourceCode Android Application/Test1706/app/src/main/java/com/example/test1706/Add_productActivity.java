package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.test1706.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Random;

public class Add_productActivity extends AppCompatActivity {

    EditText tv_productname, tv_price, tv_url_image, tv_category, tv_url_image_night, tv_quantity, tv_discount;
    Spinner spinner_category;
    Button btn_chooseImg, btn_chooseImg_Night, btn_add_product;
    ImageView img_choosen, img_chossen_night;
    private static final String TAG = "Add_productActivity";
    DatabaseReference myRef;
    FirebaseDatabase database;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category_nitewatch, R.layout.spinner_item_dark);
        init();
        tv_url_image.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_url_image.getText().toString().contains("https://") && tv_url_image.getText().toString().contains("%2F")) {
                    getInformation(tv_url_image.getText().toString());
                    hideKeyboard();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        Random rand = new Random();
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        product = new Product();

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setProduct_Name(tv_productname.getText().toString());
                product.setImage(tv_url_image.getText().toString());
                product.setImage_Night(tv_url_image_night.getText().toString());
                product.setCategory(tv_category.getText().toString());
                product.setDescription("");

                product.setQuantity(Integer.parseInt(tv_quantity.getText().toString()));
                product.setDiscount(Integer.parseInt(tv_discount.getText().toString()));
                myRef.child("NiteWatch").child(tv_category.getText().toString()).child(tv_productname.getText().toString()).setValue(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Write was successful!
                                finish();
                                // ...
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Write failed
                                Toast.makeText(Add_productActivity.this, "That bai , vui long thu lai", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });;

            }
        });
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //example url :
//https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/nitewatch%2FHAWK%2Fv2_hawk_t100_listing_front_day.png?alt=media&token=ff8dc94e-bcb7-4ae4-835a-aff87785e473
    public void getInformation(String url_image) {
        if (url_image != null) {
            if (!url_image.contains("_day") && url_image.contains("_night")) {
                url_image = url_image.replace("_night", "_day");
            }

            Random rand = new Random();
            Glide.with(Add_productActivity.this)
                    .load(url_image)
                    .into(img_choosen);
            String nite = url_image.replace("day", "night");
            tv_url_image_night.setText(nite);
            Glide.with(Add_productActivity.this)
                    .load(nite)
                    .into(img_chossen_night);
            tv_category.setText(url_image.split("%2F")[1]);   //HAWK
            String name = url_image.split("%2F")[2].split("_listing_")[0];//v2_hawk_t100
            String[] cutName = name.split("_");
            String finalname = cutName[cutName.length - 2].substring(0, 1).toUpperCase() + cutName[cutName.length - 2].substring(1).toLowerCase() + " " + cutName[cutName.length - 1];
            tv_productname.setText(finalname);
            tv_price.setText(String.valueOf(getRandomNumberInRange(10, 80) * 10));
            tv_discount.setText(String.valueOf(getRandomNumberInRange(0, 3) * 10));
            tv_quantity.setText(String.valueOf(getRandomNumberInRange(1, 20)));
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                DeleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
