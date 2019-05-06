package com.example.test1706;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.example.test1706.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminSanPham_Recycle_Adapter_NiteWatch extends RecyclerView.Adapter<AdminSanPham_Recycle_Adapter_NiteWatch.ViewHolder> {
    private final int currentlayout;
    private List<Product> list_data;
    private Context mContext;
    private static final String TAG = "Product_Recycle_Adapter";
    private boolean isNight;
    private int opening_menu = -1;
    private int old_opening_menu = -1;


    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public AdminSanPham_Recycle_Adapter_NiteWatch(Context mContext, List<Product> list_data, int currentlayout) {
        this.list_data = list_data;
        this.mContext = mContext;
        this.currentlayout = currentlayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_admin_sanpham_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

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

        viewHolder.mName.setText(list_data.get(i).getProduct_Name());
        viewHolder.mCategory.setText(list_data.get(i).getCategory());
        viewHolder.mPrice.setText(((String) ("Price:" + "$" + list_data.get(i).getPrice())));
        viewHolder.mDiscount.setText(((String) ("Discount:" + list_data.get(i).getDiscount() + "%")));
        viewHolder.mQuantity.setText(((String) ("Quantity:" + list_data.get(i).getQuantity())));
        viewHolder.mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("xóa");
                builder.setMessage("Bạn có muốn xóa " +productt.getProduct_Name()+" không ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database;
                        DatabaseReference myRef;
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference();
                        myRef.child("NiteWatch").child(productt.getCategory()).child(productt.getProduct_Name()).removeValue();
                        Intent refresh = new Intent(mContext,AdminSanPham_Activity.class);
                        mContext.startActivity(refresh);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        viewHolder.mbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit
            }
        });


        if (opening_menu == viewHolder.getAdapterPosition()) {
            //di chuyển sang trái
            viewHolder.layout_info.animate().translationXBy(300).translationX(-viewHolder.menu_product.getWidth()).setDuration(200).start();

        } else if (old_opening_menu == viewHolder.getAdapterPosition()) {
            //về vị trí cũ
            viewHolder.layout_info.animate().translationX(viewHolder.menu_product.getWidth()).translationX(0).setDuration(200).start();
        } else {
            viewHolder.layout_info.animate().translationX(0).setDuration(0).start();
        }


        viewHolder.layout_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opening_menu == viewHolder.getAdapterPosition()) {
                    //về vị trí cũ
                    viewHolder.layout_info.animate().translationX(viewHolder.menu_product.getWidth()).translationX(0).setDuration(100).start();
                    opening_menu = -1;

                } else {
                    int temp = opening_menu;
                    opening_menu = viewHolder.getAdapterPosition();
                    if (temp != -1) {
                        old_opening_menu = temp;
                    }
                    Log.d(TAG, "onClick: opening_menu:" + opening_menu);
                    Log.d(TAG, "onClick: old_opening_menu:" + temp);


                    notifyDataSetChanged();
                }

            }
        });
    }

    private void showMenu(ViewHolder viewHolder) {
        ViewGroup.MarginLayoutParams layoutParams_info = (ViewGroup.MarginLayoutParams) viewHolder.layout_info.getLayoutParams();
        if (layoutParams_info.getMarginEnd() != viewHolder.menu_product.getWidth()) {
            layoutParams_info.setMargins(-viewHolder.menu_product.getWidth(), 0, viewHolder.menu_product.getWidth(), 0);
            viewHolder.layout_info.requestLayout();
        }


    }

    private void hideMenu(ViewHolder viewHolder) {
        ViewGroup.MarginLayoutParams layoutParams_info = (ViewGroup.MarginLayoutParams) viewHolder.layout_info.getLayoutParams();
        if (layoutParams_info.getMarginEnd() == viewHolder.menu_product.getWidth()) {
            layoutParams_info.setMargins(0, 0, 0, 0);
            viewHolder.layout_info.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPrice, mCategory, mQuantity, mDiscount;
        ImageView mImage, mImageNight;
        Button mbtnEdit, mbtnDelete;
        LinearLayout layout_info, menu_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mQuantity = (TextView) itemView.findViewById(R.id.tv_tv_Quantity);
            mDiscount = (TextView) itemView.findViewById(R.id.tv_discount);
            menu_product = (LinearLayout) itemView.findViewById(R.id.menu_product);
            layout_info = (LinearLayout) itemView.findViewById(R.id.layout_product);
            mName = (TextView) itemView.findViewById(R.id.tv_horizontal_name);
            mPrice = (TextView) itemView.findViewById(R.id.tv_horizontal_price);
            mCategory = (TextView) itemView.findViewById(R.id.tv_horizontal_category);
            mImage = (ImageView) itemView.findViewById(R.id.img_horizontal_product);
            mbtnEdit = (Button) itemView.findViewById(R.id.btn_edit_product);
            mbtnDelete = (Button) itemView.findViewById(R.id.btn_delete);
            mImageNight = (ImageView) itemView.findViewById(R.id.img_horizontal_product_night);
        }


    }
}
