package com.example.test1706.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.test1706.R;
import com.example.test1706.model.Product;

import java.util.List;

public class NiteWatchAdapter extends BaseAdapter implements Filterable {
    private List<Product> list_product;
    private Context context;
    private LayoutInflater layoutInflater;

    public NiteWatchAdapter(Context context, List<Product> list_product) {
        this.context = context;
        this.list_product = list_product;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_product != null ? list_product.size() : 0;
    }

    @Override
    public Product getItem(int position) {
        return list_product.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_layout_watch_nitewatch, null);
            holder = new ViewHolder();
            holder.mImage = (ImageView) convertView.findViewById(R.id.img_product);
            holder.tvName = (TextView) convertView.findViewById(R.id.txt_product_name);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.txt_category);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.txt_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Product product = this.list_product.get(position);
        if (product != null) {
            holder.tvName.setText(product.getProduct_Name());
            holder.tvCategory.setText(product.getCategory());
            holder.tvPrice.setText((String) ("$" + product.getPrice()));

            Glide.with(context)
                    .load(product.getImage())
                    .into(holder.mImage);

        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView tvName, tvCategory, tvPrice;
        ImageView mImage;
    }

    public int FindIdOfImage(String FileName) {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(FileName, "drawable", pkgName);
        if (resID == 0) {
            resID = context.getResources().getIdentifier("ic_launcher_round", "mipmap", pkgName);
        }
        return resID;
    }
}
