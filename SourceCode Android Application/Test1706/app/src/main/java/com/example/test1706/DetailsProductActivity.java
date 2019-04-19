package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class DetailsProductActivity extends AppCompatActivity {
    private static final String TAG = "DetailsProductActivity";
    TextView mName, mPrice, mCategory;
    ImageView mImage;
    Context mContext;
    private SlidrInterface slidr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);

        slidr = Slidr.attach(this);

        mName = (TextView) findViewById(R.id.tv_productname_details);
        mCategory = (TextView) findViewById(R.id.tv_productcategory_details);
        mPrice = (TextView) findViewById(R.id.tv_productprice_details);
        mImage = (ImageView) findViewById(R.id.img_details_nitewatch);
        Log.d(TAG, "init: đã tạo tham chiếu thành công");

        Bundle b = getIntent().getExtras();
        mName.setText(b.getString("ProductName"));
        mPrice.setText("$" + b.getInt("Price"));
        mCategory.setText(b.getString("Category"));
        Glide.with(this)
                .load(b.getString("Image"))
                .apply(new RequestOptions().fitCenter())
                .into(mImage);


    }

    private void init() {

        mName = (TextView) findViewById(R.id.tv_productname_details);
        mCategory = (TextView) findViewById(R.id.tv_productcategory_details);
        mPrice = (TextView) findViewById(R.id.tv_productprice_details);
        mImage = (ImageView) findViewById(R.id.img_details_nitewatch);
        Log.d(TAG, "init: đã tạo tham chiếu thành công");
    }
}
