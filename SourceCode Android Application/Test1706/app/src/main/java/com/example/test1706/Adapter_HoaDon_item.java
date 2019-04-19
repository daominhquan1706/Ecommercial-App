package com.example.test1706;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test1706.model.Orders;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Adapter_HoaDon_item extends BaseAdapter implements Filterable {
    private List<Orders> list_orders;
    private Context context;
    private LayoutInflater layoutInflater;
    private static final String TAG = "Adapter_HoaDon_item";

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
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.tv_customer_sdt = (TextView) convertView.findViewById(R.id.tv_customer_sdt);
            holder.tv_creation_time = (TextView) convertView.findViewById(R.id.tv_creation_time);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_payid = (TextView) convertView.findViewById(R.id.tv_payid);
            holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_customer_address = (TextView) convertView.findViewById(R.id.tv_customer_address);
            holder.tv_total_price_orders_item = (TextView) convertView.findViewById(R.id.tv_total_price_orders_item);
            holder.layout_admin_hoadon_item = (RelativeLayout) convertView.findViewById(R.id.layout_admin_hoadon_item);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_HoaDon_item.ViewHolder) convertView.getTag();
        }


        Orders orders_item = this.list_orders.get(position);
        if (orders_item != null) {
            holder.tv_position.setText(String.valueOf("#" + (position + 1)));
            holder.tv_total_price_orders_item.setText(String.valueOf("$" + orders_item.getTotal()));
            holder.tv_payid.setText(orders_item.getPaymentid());
            holder.tv_status.setText(orders_item.getStatus());
            holder.tv_creation_time.setText(ThoiGian(orders_item.getCreationTime()));
            holder.tv_customer_name.setText(orders_item.getCustomerName());
            holder.tv_customer_address.setText(orders_item.getCustomerAddress());
            holder.tv_customer_sdt.setText(orders_item.getCustomerPhoneNumber());
            holder.layout_admin_hoadon_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: đã gọi được hàm click View");
                    Intent intent = new Intent(context, Admin_HoaDon_Details_activity.class);
                    Bundle b = new Bundle();
                    b.putString("PaymentId", orders_item.getPaymentid());
                    intent.putExtras(b);
                    context.startActivity(intent);
                    Log.d(TAG, "onClick: đã mở được trang details");
                }
            });

        }
        return convertView;
    }

    private String ThoiGian(long date) {
        String thoigian = "";
        Date datetime = new Date();
        datetime.setTime(date);
        Date currentday = new Date();
        long diffInMillies = Math.abs(datetime.getTime() - currentday.getTime());
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff / (60 * 60 * 24 * 30) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24 * 30)) + " tháng trước";
        } else if (diff / (60 * 60 * 24) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24)) + " ngày trước";
        } else if (diff / (60 * 60) > 0) {
            thoigian = Math.round(diff / (60 * 60)) + " giờ trước";
        } else if (diff / (60) > 0) {
            thoigian = Math.round(diff / (60)) + " phút trước";
        } else if (diff > 0) {
            thoigian = Math.round(diff) + " giây trước";
        } else {
            thoigian = "vừa xong";
        }

        return thoigian;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView
                tv_position,
                tv_customer_sdt,
                tv_total_price_orders_item,
                tv_creation_time,
                tv_status,
                tv_payid,
                tv_customer_name,
                tv_customer_address;
        RelativeLayout layout_admin_hoadon_item;
    }

}
