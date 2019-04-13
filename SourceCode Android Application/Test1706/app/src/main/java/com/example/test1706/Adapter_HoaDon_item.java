package com.example.test1706;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.test1706.model.Orders;

import java.util.List;

public class Adapter_HoaDon_item extends BaseAdapter implements Filterable {
    private List<Orders> list_orders;
    private Context context;
    private LayoutInflater layoutInflater;
    public Adapter_HoaDon_item(Context context, List<Orders> list_orders) {
        this.context = context;
        this.list_orders = list_orders;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_orders != null ? list_orders.size() : 0;
    }

    @Override
    public Orders getItem(int position) {
        return list_orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Adapter_HoaDon_item.ViewHolder holder;


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_admin_hoadon, null);
            holder = new Adapter_HoaDon_item.ViewHolder();
            holder.tv_payid = (TextView) convertView.findViewById(R.id.tv_payid);
            holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_customer_address = (TextView) convertView.findViewById(R.id.tv_customer_address);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_HoaDon_item.ViewHolder) convertView.getTag();
        }


        Orders orders_item = this.list_orders.get(position);
        if (orders_item != null) {

            holder.tv_customer_name.setText(orders_item.getCustomerName());
            holder.tv_customer_address.setText(orders_item.getCustomerAddress());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView
                tv_payid,
                tv_customer_name,
                tv_customer_address;
    }

}
