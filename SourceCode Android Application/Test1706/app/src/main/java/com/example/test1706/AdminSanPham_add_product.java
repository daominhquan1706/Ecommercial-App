package com.example.test1706;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AdminSanPham_add_product extends AppCompatActivity {

    EditText tv_productname, tv_price, tv_url_image, tv_category, tv_url_image_night, tv_quantity, tv_discount;
    Spinner spinner_category;
    Button btn_chooseImg, btn_chooseImg_Night, btn_add_product;
    ImageView img_choosen, img_chossen_night;
    private static final String TAG = "AdminSanPham_add_product";
    DatabaseReference myRef;
    FirebaseDatabase database;
    Product product;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_IMAGE_NIGHT_REQUEST = 235;
    Button btn_random_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sanpham_add);
        Slidr.attach(this);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category_nitewatch, R.layout.spinner_item_dark);
        init();
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
        myRef = database.getReference();
        product = new Product();

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (full_info()) {
                    uploadFile_Day();
                }

            }
        });

    }

    private boolean full_info() {
        boolean full_info = true;
        if (tv_productname.getText().toString().isEmpty()) {
            tv_productname.setError("không được được để trống");
            full_info = false;
        }
        if (img_choosen.getDrawable() == null) {
            Toast.makeText(this, "vui lòng chọn hình Day", Toast.LENGTH_SHORT).show();
            full_info = false;
        }
        if (img_chossen_night.getDrawable() == null) {
            tv_url_image_night.setError("vui lòng chọn hình Night");
            full_info = false;
        }
        if (spinner_category.getSelectedItem().toString().isEmpty()) {
            TextView errorText = (TextView) spinner_category.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("my actual error text");//changes the selected item text to this
            full_info = false;
        }
        if (tv_quantity.getText().toString().isEmpty()) {
            tv_quantity.setError("không được được để trống");
            full_info = false;
        }
        if (tv_discount.getText().toString().isEmpty()) {
            tv_discount.setError("không được được để trống");
            full_info = false;
        }
        return full_info;
    }

    private void ThemSanPham() {
        if (full_info()) {
            product.setProduct_Name(tv_productname.getText().toString());
            product.setImage(tv_url_image.getText().toString());
            product.setImage_Night(tv_url_image_night.getText().toString());
            product.setCategory(spinner_category.getSelectedItem().toString());
            product.setDescription("");
            product.setQuantity(Integer.parseInt(tv_quantity.getText().toString()));
            product.setDiscount(Integer.parseInt(tv_discount.getText().toString()));
            myRef.child("NiteWatch").child(spinner_category.getSelectedItem().toString()).child(tv_productname.getText().toString()).setValue(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Intent i = new Intent(AdminSanPham_add_product.this,AdminSanPham_Activity.class);
                            startActivity(i);
                            finish();
                            // ...
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            Toast.makeText(AdminSanPham_add_product.this, "That bai , vui long thu lai", Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    });
        }

    }


    //example url :
//https://firebasestorage.googleapis.com/v0/b/test1706-8ed39.appspot.com/o/nitewatch%2FHAWK%2Fv2_hawk_t100_listing_front_day.png?alt=media&token=ff8dc94e-bcb7-4ae4-835a-aff87785e473
    public void getRanDomInfo() {

        Random rand = new Random();
        String finalname = spinner_category.getSelectedItem().toString() + " " + getRandomNumberInRange(100, 999);
        finalname = finalname.substring(0,1).toUpperCase() + finalname.substring(1).toLowerCase();

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
        btn_random_info = (Button) findViewById(R.id.btn_random_info);
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_NIGHT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage_Night = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage_Night);
                img_chossen_night.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Uri uriImage_Day;
    private Uri uriImage_Night;
    String TenHinh_DAY;
    String TenHinh_NIGHT;

    private void uploadFile_Day() {
        //if there is a file to upload
        if (uriImage_Day != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Đang tải lên Firebase hình DAY");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            TenHinh_DAY = "NiteWatch/" + spinner_category.getSelectedItem().toString() + "/" + System.currentTimeMillis() + "_DAY.png";
            StorageReference riversRef = storageReference.child(TenHinh_DAY);
            riversRef.putFile(uriImage_Day)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    tv_url_image.setText(uri.toString());
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
                            progressDialog.dismiss();

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
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(this, "tải hình DAY thất bại !", Toast.LENGTH_SHORT).show();
            //you can display an error toast
        }
    }


    private void uploadFile_Night() {
        //if there is a file to upload
        if (uriImage_Night != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Đang tải lên Firebase hình NIGHT");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            TenHinh_NIGHT = "NiteWatch/" + spinner_category.getSelectedItem().toString() + "/" + System.currentTimeMillis() + "_NIGHT.png";
            StorageReference riversRef = storageReference.child(TenHinh_NIGHT);
            riversRef.putFile(uriImage_Night)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
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
                            progressDialog.dismiss();

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
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(this, "tải hình NIGHT thất bại !", Toast.LENGTH_SHORT).show();
            //you can display an error toast
        }
    }
}
