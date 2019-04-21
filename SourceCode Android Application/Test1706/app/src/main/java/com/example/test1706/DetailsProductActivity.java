package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.CommentProduct;
import com.example.test1706.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsProductActivity extends AppCompatActivity {
    private static final String TAG = "DetailsProductActivity";
    TextView mName, mPrice, mCategory, mdescription_product, btn_readmore;
    ImageView mImage;
    Context mContext;
    private SlidrInterface slidr;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Product product;
    ViewPager viewPager_comment;
    CommentAdapter commentAdapter;
    List<CommentProduct> listcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);
        init();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("NiteWatch");
        Bundle b = getIntent().getExtras();
        if (b != null) {
            myRef.child(Objects.requireNonNull(b.getString("ProductCategory")))
                    .child(Objects.requireNonNull(b.getString("ProductName")))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            product = dataSnapshot.getValue(Product.class);
                            LoadDataProduct();
                            /*myRef.child(Objects.requireNonNull(b.getString("ProductCategory")))
                                    .child(Objects.requireNonNull(b.getString("ProductName")))
                                    .child("commentproductlist")
                                    .push()
                                    .setValue(new CommentProduct(
                                            "asd",
                                            123214,
                                            "asdfadfs",
                                            4));*/
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            myRef.child(Objects.requireNonNull(b.getString("ProductCategory")))
                    .child(Objects.requireNonNull(b.getString("ProductName")))
                    .child("commentproductlist")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                listcomment.add(dataSnapshot1.getValue(CommentProduct.class));
                                setUpComment(listcomment);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }


        btn_readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_readmore.getText() == "Read more") {
                    mdescription_product.setMaxLines(Integer.MAX_VALUE);
                    btn_readmore.setText("Read less");
                } else {
                    mdescription_product.setMaxLines(5);
                    btn_readmore.setText("Read more");
                }

            }
        });

    }

    public void setUpComment(List<CommentProduct> commentProductList) {
        commentAdapter = new CommentAdapter(this, commentProductList);
        viewPager_comment.setAdapter(commentAdapter);
    }

    private void init() {
        mName = (TextView) findViewById(R.id.tv_productname_details);
        mCategory = (TextView) findViewById(R.id.tv_productcategory_details);
        mPrice = (TextView) findViewById(R.id.tv_productprice_details);
        mImage = (ImageView) findViewById(R.id.img_details_nitewatch);
        mdescription_product = (TextView) findViewById(R.id.tv_description_product);
        btn_readmore = (TextView) findViewById(R.id.btn_readmore);
        viewPager_comment = (ViewPager) findViewById(R.id.v_pager_comment_nitewatch);
        listcomment = new ArrayList<CommentProduct>();
        //slidr = Slidr.attach(this);


    }


    private void LoadDataProduct() {
        mName.setText(product.getProduct_Name());
        mPrice.setText("$" + product.getPrice());
        mCategory.setText(product.getCategory());
        mCategory.setText(product.getDescription());
        Glide.with(this)
                .load(product.getImage())
                .apply(new RequestOptions().fitCenter())
                .into(mImage);
    }

}
