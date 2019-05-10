package com.example.test1706;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class AdminSanPham_edit_product extends AppCompatActivity {
    EditText tv_productname, tv_price, tv_url_image, tv_category, tv_url_image_night, tv_quantity, tv_discount;
    Button btn_chooseImg, btn_chooseImg_Night, btn_add_product;
    ImageView img_choosen, img_chossen_night;
    private static final String TAG = "AdminSanPham_edit_product";
    DatabaseReference myRef;
    FirebaseDatabase database;
    Product product, new_product;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_IMAGE_NIGHT_REQUEST = 235;
    Button btn_random_info;
    String old_product_name;
    boolean isImageDay_Change;
    boolean isImageNight_Change;
    boolean dalayduocdulieu ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sanpham_edit);
        //Slidr.attach(this);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category_nitewatch, R.layout.spinner_item_dark);
        init();
        Bundle b = getIntent().getExtras();
        old_product_name = "";

        dalayduocdulieu=false;
        if (b != null) {
            old_product_name = b.getString("ProductName");
            myRef.child("NiteWatch").child(Objects.requireNonNull(b.getString("ProductCategory")))
                    .child(Objects.requireNonNull(b.getString("ProductName")))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            product = dataSnapshot.getValue(Product.class);
                            if (product != null) {
                                if (!dalayduocdulieu){
                                    LoadDataProduct();
                                    dalayduocdulieu=true;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(AdminSanPham_edit_product.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(AdminSanPham_edit_product.this, R.string.thatbaivuilongthulai, Toast.LENGTH_SHORT).show();
            finish();
        }


        btn_random_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRanDomInfo();
            }
        });
        setupButton_ChooseImage();
        tv_url_image.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_url_image.getText().toString().contains("https://") && tv_url_image.getText().toString().contains("%2F")) {
                    //getInformation(tv_url_image.getText().toString());
                    //hideKeyboard();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        Random rand = new Random();
        // Write a message to the database
        database = FirebaseDatabase.getInstance();


        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (full_info()) {
                    uploadFile_Day();
                }

            }
        });

    }

    private void LoadDataProduct() {

        tv_productname.setText(product.getProduct_Name());
        tv_url_image.setText(product.getImage());
        tv_url_image_night.setText(product.getImage_Night());
        tv_quantity.setText(String.valueOf(product.getQuantity()));
        tv_discount.setText(String.valueOf(product.getDiscount()));
        tv_price.setText(String.valueOf(product.getPrice()));

        Glide.with(AdminSanPham_edit_product.this)
                .load(product.getImage())
                .apply(new RequestOptions().fitCenter())
                .into(img_choosen);
        Glide.with(AdminSanPham_edit_product.this)
                .load(product.getImage_Night())
                .apply(new RequestOptions().fitCenter())
                .into(img_chossen_night);

    }

    private boolean full_info() {
        boolean full_info = true;
        if (tv_productname.getText().toString().isEmpty()) {
            tv_productname.setError(getString(R.string.khongduocdetrong));
            full_info = false;
        }
        if (img_choosen.getDrawable() == null) {
            Toast.makeText(this, getString(R.string.vuilongchonhinhday), Toast.LENGTH_SHORT).show();
            full_info = false;
        }
        if (img_chossen_night.getDrawable() == null) {
            tv_url_image_night.setError(getString(R.string.vuilongchonhinhnight));
            full_info = false;
        }
        if (tv_quantity.getText().toString().isEmpty()) {
            tv_quantity.setError(getString(R.string.khongduocdetrong));
            full_info = false;
        }
        if (tv_discount.getText().toString().isEmpty()) {
            tv_discount.setError(getString(R.string.khongduocdetrong));
            full_info = false;
        }
        return full_info;
    }

    private void ThemSanPham() {
        if (!old_product_name.equals("")) {
            new_product = product;
            myRef.child("NiteWatch").child(new_product.getCategory()).child(old_product_name).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    new_product.setProduct_Name(tv_productname.getText().toString());
                    new_product.setImage(tv_url_image.getText().toString());
                    new_product.setImage_Night(tv_url_image_night.getText().toString());
                    new_product.setQuantity(Integer.parseInt(tv_quantity.getText().toString()));
                    new_product.setDiscount(Integer.parseInt(tv_discount.getText().toString()));
                    new_product.setPrice(Integer.parseInt(tv_price.getText().toString()));
                    myRef.child("NiteWatch").child(new_product.getCategory()).child(tv_productname.getText().toString()).setValue(new_product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    progress.dismiss();
                                    finish();
                                    // ...
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    Toast.makeText(AdminSanPham_edit_product.this, getString(R.string.thatbaivuilongthulai), Toast.LENGTH_SHORT).show();
                                    // ...
                                }
                            });
                }

            });
        }


    }


    //example url :
//https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/nitewatch%2FHAWK%2Fv2_hawk_t100_listing_front_day.png?alt=media&token=ff8dc94e-bcb7-4ae4-835a-aff87785e473
    public void getRanDomInfo() {

        Random rand = new Random();
        String finalname = product.getCategory() + " " + getRandomNumberInRange(100, 999);
        finalname = finalname.substring(0, 1).toUpperCase() + finalname.substring(1).toLowerCase();

        tv_productname.setText(finalname);
        tv_price.setText(String.valueOf(getRandomNumberInRange(10, 80) * 10));
        tv_discount.setText(String.valueOf(getRandomNumberInRange(0, 3) * 10));
        tv_quantity.setText(String.valueOf(getRandomNumberInRange(1, 20)));

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
        isImageDay_Change = false;
        isImageNight_Change = false;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        product = new Product();
        new_product = new Product();
        btn_random_info = (Button) findViewById(R.id.btn_random_info);
        tv_url_image_night = (EditText) findViewById(R.id.tv_url_image_night);
        tv_category = (EditText) findViewById(R.id.tv_Category);
        tv_url_image = (EditText) findViewById(R.id.tv_url_image);
        tv_productname = (EditText) findViewById(R.id.tv_productname_add);
        tv_price = (EditText) findViewById(R.id.tv_product_price_add);
        btn_chooseImg = (Button) findViewById(R.id.btn_choose_img);
        btn_chooseImg_Night = (Button) findViewById(R.id.btn_choose_img_night);
        img_choosen = (ImageView) findViewById(R.id.img_choose);
        img_chossen_night = (ImageView) findViewById(R.id.img_choose_night);
        btn_add_product = (Button) findViewById(R.id.btn_add_product);
        tv_quantity = (EditText) findViewById(R.id.tv_quantity);
        tv_discount = (EditText) findViewById(R.id.tv_discount);
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setupButton_ChooseImage() {

        img_choosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        img_chossen_night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser_Night();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ban ngày"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooser_Night() {
        Intent intent = new Intent();
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ban đêm"), PICK_IMAGE_NIGHT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage_Day = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage_Day);
                img_choosen.setImageBitmap(bitmap);
                isImageDay_Change = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_NIGHT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage_Night = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage_Night);
                img_chossen_night.setImageBitmap(bitmap);
                isImageNight_Change = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Uri uriImage_Day;
    private Uri uriImage_Night;
    String TenHinh_DAY;
    String TenHinh_NIGHT;
    ProgressDialog progressDialog_day;
    ProgressDialog progress;
    private void uploadFile_Day() {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.dangthaydoi));
        progress.show();

        if (isImageDay_Change) {
            progressDialog_day = new ProgressDialog(this);
            progressDialog_day.setMessage(getString(R.string.dangtailenhinhday));
            progressDialog_day.show();

            //if there is a file to upload
            if (uriImage_Day != null) {
                //displaying a progress dialog while upload is going on

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                TenHinh_DAY = "NiteWatch/" + product.getCategory() + "/" + System.currentTimeMillis() + "_DAY.png";
                StorageReference riversRef = storageReference.child(TenHinh_DAY);
                riversRef.putFile(uriImage_Day)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog_day.dismiss();
                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), getString(R.string.tailenthanhcong), Toast.LENGTH_LONG).show();
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        tv_url_image.setText(uri.toString());
                                        progressDialog_night.setTitle(getString(R.string.dangtailenhinhnight));
                                        progressDialog_night.show();
                                        uploadFile_Night();

                                        //Do what you want with the url
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog_day.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog_day.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });
            }
            //if there is not any file
            else {
                Toast.makeText(this, getString(R.string.taihinhdaythatbai), Toast.LENGTH_SHORT).show();
                //you can display an error toast
            }
        } else {
            progressDialog_night = new ProgressDialog(this);
            progressDialog_night.setMessage(getString(R.string.dangtailenhinhnight));
            progressDialog_night.show();
            uploadFile_Night();
        }


    }

    ProgressDialog progressDialog_night;

    private void uploadFile_Night() {
        if (!isImageNight_Change) {
            progressDialog_night.dismiss();
            ThemSanPham();
        }

        else if (uriImage_Night != null) {
            //displaying a progress dialog while upload is going on

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            TenHinh_NIGHT = "NiteWatch/" + product.getCategory() + "/" + System.currentTimeMillis() + "_NIGHT.png";
            StorageReference riversRef = storageReference.child(TenHinh_NIGHT);
            riversRef.putFile(uriImage_Night)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog_night.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), R.string.tailenthanhcong, Toast.LENGTH_LONG).show();
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    tv_url_image_night.setText(uri.toString());
                                    //Do what you want with the url
                                    ThemSanPham();
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog_night.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog_night.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(this, getString(R.string.taihinhnightthatbai), Toast.LENGTH_SHORT).show();
            //you can display an error toast
        }
    }
}
