package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Add_productActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    public static final int PICK_IMAGE2 = 2;

    TextView tv_productname, tv_price;
    Spinner spinner_category;
    Button btn_chooseImg, btn_chooseImg_Night;
    ImageView img_choosen, img_chossen_night;
    private static final String TAG = "Add_productActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category_nitewatch, R.layout.spinner_item_dark);
        init();

        btn_chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }

        });

        btn_chooseImg_Night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE2);

            }

        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        assert selectedImage != null;
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    img_choosen.setImageURI(selectedImage);// To display selected image in image view
                    tv_productname.setText(selectedImage.getPath());
                    File file =new File(selectedImage.getPath());
                    DeleteRecursive(file);

                    break;
                case PICK_IMAGE2:
                    Uri selectedImage2 = data.getData();
                    InputStream imageStream2 = null;
                    try {
                        assert selectedImage2 != null;
                        imageStream = getContentResolver().openInputStream(selectedImage2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage2 = BitmapFactory.decodeStream(imageStream2);
                    img_chossen_night.setImageURI(selectedImage2);// To display selected image in image view
                    Toast.makeText(this, "đã chọn đồng hồ ban đêm", Toast.LENGTH_SHORT).show();
                default:
                    Toast.makeText(this, "nothing change , this is the default case", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "nothing change", Toast.LENGTH_SHORT).show();
        }
    }
    public static void DeleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                DeleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    private void init() {
        tv_productname = (TextView) findViewById(R.id.tv_productname_add);
        tv_price = (TextView) findViewById(R.id.tv_product_price_add);
        spinner_category = (Spinner) findViewById(R.id.spinner_add);
        btn_chooseImg = (Button) findViewById(R.id.btn_choose_img);
        btn_chooseImg_Night = (Button) findViewById(R.id.btn_choose_img_night);
        img_choosen = (ImageView) findViewById(R.id.img_choose);
        img_chossen_night = (ImageView) findViewById(R.id.img_choose_night);
    }


}
