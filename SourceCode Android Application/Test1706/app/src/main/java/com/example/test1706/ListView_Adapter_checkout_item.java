package com.example.test1706;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.test1706.model.Cart;

import java.util.List;

public class ListView_Adapter_checkout_item extends BaseAdapter implements Filterable {
    private List<Cart> list_product;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListView_Adapter_checkout_item(Context context, List<Cart> list_product) {
        this.context = context;
        this.list_product = list_product;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_product != null ? list_product.size() : 0;
    }

    @Override
    public Cart getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.item_checkout_item, null);
            holder = new ViewHolder();

            holder.tv_name_checkout = (TextView) convertView.findViewById(R.id.tv_name_checkout);
            holder.tv_product_price_checkout = (TextView) convertView.findViewById(R.id.tv_product_price_checkout);
            holder.tv_total_price_checkout = (TextView) convertView.findViewById(R.id.tv_total_price_checkout);
            holder.tv_quantity_checkout = (TextView) convertView.findViewById(R.id.tv_quantity_checkout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Cart cart_item = this.list_product.get(position);
        if (cart_item != null) {
            holder.tv_name_checkout.setText(cart_item.getProductName());
            holder.tv_product_price_checkout.setText(String.valueOf((int)cart_item.getPrice()));
            holder.tv_quantity_checkout.setText(String.valueOf(cart_item.getQuantity()));
            holder.tv_total_price_checkout.setText(String.valueOf((int)cart_item.getPrice() * cart_item.getQuantity()));


        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView
                tv_name_checkout,
                tv_product_price_checkout,
                tv_total_price_checkout,
                tv_quantity_checkout;
    }

}
