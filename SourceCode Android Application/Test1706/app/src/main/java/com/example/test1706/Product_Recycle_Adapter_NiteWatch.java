package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Product;

import org.w3c.dom.Text;

import java.util.List;

public class Product_Recycle_Adapter_NiteWatch extends RecyclerView.Adapter<Product_Recycle_Adapter_NiteWatch.ViewHolder> {
    private List<Product> list_data;
    private Context mContext;
    private static final String TAG = "Product_Recycle_Adapter";
    private boolean isNight;
    private int currentlayout;
private TextView textCartItemCount;

    public void setTextCartItemCount(TextView textCartItemCount) {
        this.textCartItemCount = textCartItemCount;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public Product_Recycle_Adapter_NiteWatch(Context mContext, List<Product> list_data, int currentlayout) {
        this.list_data = list_data;
        this.mContext = mContext;
        this.currentlayout = currentlayout;
        Log.d(TAG, "onCreateViewHolder: listdata()  ");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: đã được gọi ");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(currentlayout, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder :đã được gọi");
        final Product productt = list_data.get(i);
        if (productt == null) {
            return;
        }
        Glide.with(mContext)
                .load(list_data.get(i).getImage())
                .apply(new RequestOptions().fitCenter())
                .into(viewHolder.mImage);
        Glide.with(mContext)
                .load(list_data.get(i).getImage_Night())
                .apply(new RequestOptions().fitCenter())
                .into(viewHolder.mImageNight);

        long time = 1000;
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(time);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(50);
        fadeOut.setDuration(time);

        if (isNight && viewHolder.mImage.getVisibility()==View.VISIBLE) {
            viewHolder.mImage.setAnimation(fadeOut);
            viewHolder.mImage.setVisibility(View.INVISIBLE);
            viewHolder.mImageNight.setVisibility(View.VISIBLE);
            viewHolder.mImageNight.setAnimation(fadeIn);
            viewHolder.mlayout_horizontal_nitewatch_item.setBackgroundColor(mContext.getResources().getColor(R.color.clearblack));

        } else if (!isNight && viewHolder.mImageNight.getVisibility()==View.VISIBLE){
            viewHolder.mImageNight.setAnimation(fadeOut);
            viewHolder.mImageNight.setVisibility(View.INVISIBLE);
            viewHolder.mImage.setVisibility(View.VISIBLE);
            viewHolder.mImage.setAnimation(fadeIn);
            viewHolder.mlayout_horizontal_nitewatch_item.setBackgroundColor(mContext.getResources().getColor(R.color.black_cardview_nitewatch));
        }

        viewHolder.mName.setText(list_data.get(i).getProduct_Name());
        viewHolder.mCategory.setText(list_data.get(i).getCategory());
        viewHolder.mPrice.setText(((String) ("$" + list_data.get(i).getPrice())));

        viewHolder.mbtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: đã gọi được hàm click View");
                Intent intent = new Intent(mContext, DetailsProductActivity.class);
                Bundle b = new Bundle();
                b.putString("ProductName", productt.getProduct_Name());
                b.putString("Category", productt.getCategory());
                b.putInt("Price", productt.getPrice());
                b.putString("Image", productt.getImage());


                intent.putExtras(b);


                mContext.startActivity(intent);
                Log.d(TAG, "onClick: đã mở được trang details");
            }
        });


        viewHolder.mbtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartSqliteHelper cartSqliteHelper = new CartSqliteHelper(mContext);
                cartSqliteHelper.addCart(productt);
                Toast.makeText(mContext, "Đã thêm "+productt.getProduct_Name(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return (list_data == null) ? 0 : list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPrice, mCategory;
        ImageView mImage, mImageNight;
        Button mbtnView, mbtnCart;
        LinearLayout mlayout_horizontal_nitewatch_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                mlayout_horizontal_nitewatch_item = (LinearLayout) itemView.findViewById(R.id.layout_horizontal_nitewatch_item);
                mName = (TextView) itemView.findViewById(R.id.tv_horizontal_name);
                mPrice = (TextView) itemView.findViewById(R.id.tv_horizontal_price);
                mCategory = (TextView) itemView.findViewById(R.id.tv_horizontal_category);
                mImage = (ImageView) itemView.findViewById(R.id.img_horizontal_product);
                mbtnView = (Button) itemView.findViewById(R.id.btnview_horizontal_nitewatch);
                mbtnCart = (Button) itemView.findViewById(R.id.btncart_horizontal_nitewatch);
                mImageNight = (ImageView) itemView.findViewById(R.id.img_horizontal_product_night);
        }

    }
}
