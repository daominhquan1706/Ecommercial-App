package com.example.test1706.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.R;
import com.example.test1706.model.Product;

import java.util.List;

public class Product_Recycle_Adapter extends RecyclerView.Adapter<Product_Recycle_Adapter.ViewHolder> {
    private List<Product> list_data;
    private Context mContext;
    private static final String TAG = "Product_Recycle_Adapter";

    public Product_Recycle_Adapter(Context mContext,List<Product> list_data) {
        this.list_data = list_data;
        this.mContext = mContext;
        Log.d(TAG,"onCreateViewHolder: listdata()  ");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHolder: đã được gọi ");

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout_watch_smartwatch,viewGroup,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG,"onBindViewHolder :đã được gọi");

        Glide.with(mContext)
                .load(list_data.get(i).getImage())
                .apply(new RequestOptions().fitCenter())
                .into(viewHolder.mImage);
        viewHolder.mName.setText(list_data.get(i).getProduct_Name());
        viewHolder.mPrice.setText("$ "+ list_data.get(i).getPrice());

    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mName,mPrice;
        ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.txt_product_name);
            mPrice= (TextView) itemView.findViewById(R.id.txt_price);
            mImage = (ImageView) itemView.findViewById(R.id.img_product);
        }

    }
}
