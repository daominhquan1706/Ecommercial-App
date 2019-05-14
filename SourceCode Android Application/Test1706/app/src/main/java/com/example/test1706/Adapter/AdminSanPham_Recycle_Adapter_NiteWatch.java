package com.example.test1706.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.AdminSanPham_Activity;
import com.example.test1706.AdminSanPham_edit_product;
import com.example.test1706.R;
import com.example.test1706.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;

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
        viewHolder.mPrice.setText(((String) (mContext.getString(R.string.admin_price_product) + "$" + list_data.get(i).getPrice())));
        viewHolder.mDiscount.setText(((String) (mContext.getString(R.string.admin_discount_product) + list_data.get(i).getDiscount() + "%")));
        viewHolder.mQuantity.setText(((String) (mContext.getString(R.string.admin_quantity_product) + list_data.get(i).getQuantity())));
        viewHolder.mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.xoa_title);
                builder.setMessage(mContext.getString(R.string.dialog_message_xoa_product) + productt.getProduct_Name());
                builder.setCancelable(false);
                builder.setPositiveButton(mContext.getString(R.string.answer_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton(mContext.getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database;
                        DatabaseReference myRef;
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference();
                        myRef.child("NiteWatch").child(productt.getCategory()).child(productt.getProduct_Name()).removeValue();
                        Intent refresh = new Intent(mContext, AdminSanPham_Activity.class);
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
                Intent intent_edit = new Intent(mContext, AdminSanPham_edit_product.class);
                Bundle b = new Bundle();
                b.putString("ProductName", productt.getProduct_Name());
                b.putString("ProductCategory", productt.getCategory());
                intent_edit.putExtras(b);
                mContext.startActivity(intent_edit);
            }
        });

        viewHolder.folding_cell_product_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.folding_cell_product_admin.toggle(false);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPrice, mCategory, mQuantity, mDiscount;
        ImageView mImage, mImageNight;
        Button mbtnEdit, mbtnDelete;
        LinearLayout layout_product, menu_product;
        FoldingCell folding_cell_product_admin;
        RelativeLayout layout_menu_admin_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mQuantity = (TextView) itemView.findViewById(R.id.tv_tv_Quantity);
            mDiscount = (TextView) itemView.findViewById(R.id.tv_discount);
            menu_product = (LinearLayout) itemView.findViewById(R.id.menu_product);
            layout_product = (LinearLayout) itemView.findViewById(R.id.layout_product);
            mName = (TextView) itemView.findViewById(R.id.tv_horizontal_name);
            mPrice = (TextView) itemView.findViewById(R.id.tv_horizontal_price);
            mCategory = (TextView) itemView.findViewById(R.id.tv_horizontal_category);
            mImage = (ImageView) itemView.findViewById(R.id.img_horizontal_product);
            mbtnEdit = (Button) itemView.findViewById(R.id.btn_edit_product);
            mbtnDelete = (Button) itemView.findViewById(R.id.btn_delete);
            mImageNight = (ImageView) itemView.findViewById(R.id.img_horizontal_product_night);
            folding_cell_product_admin = (FoldingCell) itemView.findViewById(R.id.folding_cell_product_admin);
            layout_menu_admin_product = (RelativeLayout) itemView.findViewById(R.id.layout_menu_admin_product);
        }


    }
}