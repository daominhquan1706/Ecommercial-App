package com.example.test1706;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.test1706.model.Orders;

import java.util.List;

public class AdminHoaDon_Activity extends AppCompatActivity {
    Adapter_HoaDon_item adapter_hoaDon_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hoa_don_);
    }

}

class Adapter_HoaDon_item extends BaseAdapter {
    private List<Orders> list_order;
    private Context context;
    private LayoutInflater layoutInflater;

    public Adapter_HoaDon_item(Context context, List<Orders> list_product) {
        this.context = context;
        this.list_order = list_product;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_checkout_item, null);
            holder = new Adapter_HoaDon_item.ViewHolder();

            holder.tv_payid = (TextView) convertView.findViewById(R.id.tv_payid);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_customer_address = (TextView) convertView.findViewById(R.id.tv_customer_address);
            holder.tv_creation_time = (TextView) convertView.findViewById(R.id.tv_creation_time);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_HoaDon_item.ViewHolder) convertView.getTag();
        }


        Orders cart_item = this.list_order.get(position);
        if (cart_item != null) {
            holder.tv_payid.setText("");
            holder.tv_status.setText("");
            holder.tv_customer_name.setText("");
            holder.tv_customer_address.setText("");12q


        }
        return convertView;
    }

    static class ViewHolder {
        TextView
                tv_payid, tv_status, tv_customer_name, tv_customer_address, tv_creation_time;


        Button btn_delete, btn_accept;
    }
}
