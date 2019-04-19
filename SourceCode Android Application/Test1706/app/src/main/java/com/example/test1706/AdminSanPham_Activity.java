package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.test1706.model.Product;
import com.example.test1706.mongodb.Code_mongodb;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminSanPham_Activity extends AppCompatActivity {

    Button btn_addproduct, btn_firebase;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<Product> productList;
    List<String> mkey;
    AdminSanPham_Recycle_Adapter_NiteWatch productadapter;
    RecyclerView listView_admin_product_nitewatch;
    private static final String TAG = "AdminSanPham_Activity";
    private StorageReference mStorageRef;
    LinearLayout layout_product;
    List<Product> Hawk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sanpham);
        Slidr.attach(this);
        init();
        btn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminSanPham_Activity.this, AdminSanPham_add_product.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        // Write a message to the database


        productList = new ArrayList<Product>();
        mkey = new ArrayList<String>();

        productadapter = new AdminSanPham_Recycle_Adapter_NiteWatch(this, productList, 0);


        listView_admin_product_nitewatch.setAdapter(productadapter);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("NiteWatch").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onChildAdded: " + item.getKey());
                    Product itemProduct = item.getValue(Product.class);
                    productList.add(itemProduct);
                    mkey.add(item.getKey());
                }
                productadapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    productList.set(mkey.indexOf(item.getKey()), item.getValue(Product.class));
                    Log.d("UPDATE dữ liệu ", dataSnapshot.getValue(Product.class).getProduct_Name() + s);
                }
                productadapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Hawk = new ArrayList<Product>();

        btn_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String item : getImageHAWK()){
                    addToFireBase(item);
                }


            }
        });


    }

    private List<String> getImageHAWK() {
        List<String> listImage = new ArrayList<String>();


        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_202s_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_202s_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_202_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_202_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_zt100s_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_zt100s_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_zt100_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Falpha_zt100_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_201s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_201s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_201_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_201_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_208s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_208s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_208_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_208_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_209s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_209s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_209_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_209_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_212s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_212s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_212_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FALPHA%2Fv8_alpha_212_listing_front_night.png?alt=media");

        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_201s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_201s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_201_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_300s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_300s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_300t_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_300t_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_t100s_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_t100s_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_t100_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_t100_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_z400t_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv2_hawk_z400t_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv3_hawk_z400ts_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FHAWK%2Fv3_hawk_z400ts_listing_front_night.png?alt=media");

        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100l ob_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100l ob_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100l og_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100l og_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100s ob_listing_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100s ob_listing_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100s og_listing_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 208_t100s og_listing_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100l tan ob_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100l tan ob_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100l tan og_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100l tan og_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100s ob_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100s ob_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100s og_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon 212_t100s og_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon auto_216l_listing_front_day_category.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon auto_216l_listing_front_night_category.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon auto_216s_listing_front_day_category.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-AUTO%2Ficon auto_216s_listing_front_night_category.png?alt=media");

        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Ficon_212l tan_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Ficon_212l tan_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Ficon_212s_listing_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Ficon_212s_listing_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201l t100_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201l t100_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201l_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201l_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201s t100_listing_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201s t100_listing_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201s_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_201s_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209l t100_list_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209l t100_list_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209l_list_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209l_list_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209s t100_list_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209s t100_list_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209s_list_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FICON-QUARTZ%2Fv2 icon_209s_list_night.png?alt=media");

        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_201_listing_pvd-small_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_201_listing_pvd-small_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_212rg_listing_listing_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_212rg_list_front_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_215p_listing_ss-small_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_215p_listing_ss-small_night.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_215_listing_ss-small_day_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fmarquess_215_listing_ss-small_night_1.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fnw marquess_212ss_list_front_day.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMARQUESS%2Fnw marquess_212ss_list_front_night.png?alt=media");

        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_201s_listing_front_day_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_201s_listing_front_night_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_201_listing_front_day_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_201_listing_front_night_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_209s_listing_front_day_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_209s_listing_front_night_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_209_listing_front_day_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_209_listing_front_night_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_212s_listing_front_day_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_212s_listing_front_night_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_212_listing_front_day_04-18.png?alt=media");
        listImage.add("https://firebasestorage.googleapis.com/v0/b/ecommerial-40d25.appspot.com/o/NiteWatch%2FMX10%2Fmx10_212_listing_front_night_04-18.png?alt=media");


        return listImage;
    }

    public void addToFireBase(String url_image) {
        if (url_image != null) {
            if (!url_image.contains("_day") || url_image.contains("_night")) {
                url_image = url_image.replace("_night", "_day");
            }
            final Product product = new Product();
            Random rand = new Random();
            String nite = url_image.replace("day", "night");
            String name = url_image.split("%2F")[2].split("_list")[0];//v2_hawk_t100
            String[] cutName = name.split("_");
            String finalname = cutName[cutName.length - 2].substring(0, 1).toUpperCase() + cutName[cutName.length - 2].substring(1).toLowerCase() + " " + cutName[cutName.length - 1];
            product.setProduct_Name(finalname);
            product.setImage(url_image.replaceAll(" ", "%20"));
            product.setImage_Night(nite.replaceAll(" ", "%20"));
            product.setCategory(url_image.split("%2F")[1]);
            product.setDescription("");
            product.setPrice(getRandomNumberInRange(10, 80) * 10);
            product.setQuantity(getRandomNumberInRange(0, 3) * 10);
            product.setDiscount(getRandomNumberInRange(1, 20));
            myRef.child("NiteWatch").child(product.getCategory()).child(product.getProduct_Name()).setValue(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Đã thêm " + product.getProduct_Name());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            Toast.makeText(AdminSanPham_Activity.this, "That bai , vui long thu lai", Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    });



            if(product.getCategory()=="HAWK"){
                Hawk.add(product);
            }
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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

    private void init() {
        btn_firebase = (Button) findViewById(R.id.btn_firebase);
        layout_product = (LinearLayout) findViewById(R.id.layout_product);
        btn_addproduct = (Button) findViewById(R.id.btn_add_product);
        listView_admin_product_nitewatch = (RecyclerView) findViewById(R.id.listView_admin_product_nitewatch);
    }
}
