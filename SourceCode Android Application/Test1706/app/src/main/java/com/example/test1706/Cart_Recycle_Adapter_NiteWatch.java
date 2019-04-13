package com.example.test1706;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.Cart;
import com.example.test1706.model.CartSqliteHelper;
import com.example.test1706.model.Product;

import java.util.List;

public class Cart_Recycle_Adapter_NiteWatch extends RecyclerView.Adapter<Cart_Recycle_Adapter_NiteWatch.ViewHolder> {
    private List<Cart> list_data;
    private Context mContext;
    private static final String TAG = "Cart_Recycle_Adapter";
    private boolean isNight;
    private int currentlayout;
    private CartSqliteHelper cartSqliteHelper;
    private TextView tv_count_quantity, tv_count_price;
    private RecyclerView recycleview_cart_list;

    public void setRecycleview_cart_list(RecyclerView recycleview_cart_list) {
        this.recycleview_cart_list = recycleview_cart_list;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public void setTv_count_quantity(TextView tv_count_quantity) {
        this.tv_count_quantity = tv_count_quantity;
    }

    public void setTv_count_price(TextView tv_count_price) {
        this.tv_count_price = tv_count_price;
    }

    public Cart_Recycle_Adapter_NiteWatch(Context mContext, List<Cart> list_data, int currentlayout) {
        this.list_data = list_data;
        this.mContext = mContext;
        this.currentlayout = currentlayout;
        cartSqliteHelper = new CartSqliteHelper(mContext);
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
        final Cart cartt = list_data.get(i);
        if (cartt == null) {
            return;
        }
        Glide.with(mContext)
                .load(list_data.get(i).getImageProduct())
                .apply(new RequestOptions().fitCenter())
                .into(viewHolder.mImage);
        viewHolder.mName.setText(list_data.get(i).getProductName());
        viewHolder.mPrice.setText(((String) ("$" + list_data.get(i).getPrice())));
        viewHolder.mQuantity.setText(((String) ("" + list_data.get(i).getQuantity())));

        viewHolder.mbtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setProduct_Name(cartt.getProductName());
                cartSqliteHelper.PlusOneQuantity(product);
                list_data = cartSqliteHelper.getAllCarts();
                tv_count_price.setText(String.valueOf("$" + cartSqliteHelper.getCartPriceCount()));
                tv_count_quantity.setText(String.valueOf(cartSqliteHelper.getCartQuantityCount()));

                notifyDataSetChanged();
            }
        });
        viewHolder.mbtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setProduct_Name(cartt.getProductName());
                cartSqliteHelper.MinusOneQuantity(product);
                list_data = cartSqliteHelper.getAllCarts();
                tv_count_price.setText(String.valueOf("$" + cartSqliteHelper.getCartPriceCount()));
                tv_count_quantity.setText(String.valueOf(cartSqliteHelper.getCartQuantityCount()));
                if(cartSqliteHelper.getCartQuantityCount()==0){
                    recycleview_cart_list.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {

        return (list_data == null) ? 0 : list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPrice;
        TextView mQuantity;
        ImageView mImage, mImageNight;
        Button mbtnPlus, mbtnMinus;
        LinearLayout mlayout_horizontal_nitewatch_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.img_product_cart);
            mName = (TextView) itemView.findViewById(R.id.tv_name_cart);
            mPrice = (TextView) itemView.findViewById(R.id.tv_total_price_cart);
            mQuantity = (TextView) itemView.findViewById(R.id.edt_quantity_product_cart);
            mbtnMinus = (Button) itemView.findViewById(R.id.minus_one_product_cart);
            mbtnPlus = (Button) itemView.findViewById(R.id.plus_one_product_cart);
        }

    }
}
