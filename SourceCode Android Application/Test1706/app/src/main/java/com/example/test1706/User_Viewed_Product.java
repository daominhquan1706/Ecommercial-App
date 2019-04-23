package com.example.test1706;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.test1706.model.Product;
import com.example.test1706.model.ProductSqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class User_Viewed_Product extends AppCompatActivity {
ProductSqliteHelper productSqliteHelper;
List<Product> productList;
RecyclerView recyclerView;
Product_Recycle_Adapter_NiteWatch product_recycle_adapter_niteWatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_viewed_product);
        productSqliteHelper = new ProductSqliteHelper(this);
        productList = new ArrayList<>();
        productList = productSqliteHelper.getAllProducts();

        recyclerView = (RecyclerView) findViewById(R.id.listView_product_nitewatch);
        product_recycle_adapter_niteWatch = new Product_Recycle_Adapter_NiteWatch(
                this,
                productList,
                R.layout.item_layout_watch_nitewatch);
        recyclerView.setAdapter(product_recycle_adapter_niteWatch);

    }
}
