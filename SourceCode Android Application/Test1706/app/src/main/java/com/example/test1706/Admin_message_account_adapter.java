package com.example.test1706;

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
import com.example.test1706.model.Cart;

import java.util.List;

public class Admin_message_account_adapter extends BaseAdapter implements Filterable {
    private List<ChatMessage> list_product;
    private Context context;
    private LayoutInflater layoutInflater;

    public Admin_message_account_adapter(Context context, List<ChatMessage> list_product) {
        this.context = context;
        this.list_product = list_product;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_product != null ? list_product.size() : 0;
    }

    @Override
    public ChatMessage getItem(int position) {
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

            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_message_user = (TextView) convertView.findViewById(R.id.tv_message_user);
            holder.tv_time_message = (TextView) convertView.findViewById(R.id.tv_time_message);
            holder.img_user_avatar_chat = (ImageView) convertView.findViewById(R.id.img_user_avatar_chat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ChatMessage chat_item = this.list_product.get(position);
        if (chat_item != null) {
            holder.tv_username.setText(chat_item.getMessageUser());
            holder.tv_message_user.setText(String.valueOf(chat_item.getMessageText()));
            holder.tv_time_message.setText(String.valueOf(chat_item.getMessageTime()));





        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView
                tv_username,
                tv_message_user,
                tv_time_message;
        ImageView
                img_user_avatar_chat;
    }

}
