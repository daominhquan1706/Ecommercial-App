package com.example.test1706;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1706.model.Product;

import java.util.ArrayList;
import java.util.List;

public class RecycleView_Product extends RecyclerView.Adapter<RecycleView_Product.ProductViewHolder> {
    private List<Product> productlist;
    private Activity activity;
    private Context context;
    /**
     * Contructor
     */
    public RecycleView_Product(Activity activity, List<Product> bookList) {
        this.activity = activity;
        this.productlist = bookList;
    }

    /**
     * Create ViewHolder
     */
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName;
        private TextView tvCategory;
        private TextView tvPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            tvProductName = (TextView) itemView.findViewById(R.id.txt_product_name);
            tvCategory = (TextView) itemView.findViewById(R.id.txt_category);
            tvPrice = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /** Get layout */
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dong_ho_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        /** Set Value*/
        final Product product = productlist.get(position);
        holder.tvProductName.setText(product.getProduct_Name());
        holder.tvCategory.setText(product.getCategory());
        holder.imgProduct.setImageResource(FindIdOfImage(product.getImage()));
        /*Sự kiện click vào item*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, product.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }
    public int FindIdOfImage(String FileName){
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(FileName , "mipmap", pkgName);
        if(resID==0){
            resID=context.getResources().getIdentifier("ic_launcher_round" , "mipmap", pkgName);
        }
        return resID;
    }
}


