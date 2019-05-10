package com.example.test1706;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test1706.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_message_account_adapter extends BaseAdapter implements Filterable {
    private List<ChatMessage> list_product;
    private Context context;
    private LayoutInflater layoutInflater;
    private static final String TAG = "Admin_message_account_a";
    FirebaseAuth firebaseAuth;
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
            convertView = layoutInflater.inflate(R.layout.item_account_user_vertical, null);
            holder = new ViewHolder();

            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_message_user = (TextView) convertView.findViewById(R.id.tv_message_user);
            holder.tv_time_message = (TextView) convertView.findViewById(R.id.tv_time_message);
            holder.img_user_avatar_chat = (CircleImageView) convertView.findViewById(R.id.img_user_avatar_chat);
            holder.layout_account_message_user = (LinearLayout) convertView.findViewById(R.id.layout_account_message_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChatMessage chat_item = this.list_product.get(position);
        if (chat_item != null) {
            Glide.with(context)
                    .load("https://api.adorable.io/avatars/"+chat_item.getUserUID().toString()+"@adorable.png")
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.img_user_avatar_chat);
            holder.tv_username.setText(chat_item.getMessageUser());
            holder.tv_message_user.setText(String.valueOf(chat_item.getMessageText()));
            holder.tv_time_message.setText(ThoiGianChat(chat_item.getMessageTime()));
            holder.layout_account_message_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: đã gọi được hàm click View");
                    Intent intent = new Intent(context, Admin_Message_chat_with_user.class);
                    Bundle b = new Bundle();
                    b.putString("userUID", chat_item.getUserUID());
                    b.putString("userEmail", chat_item.getMessageUser());
                    intent.putExtras(b);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    Log.d(TAG, "onClick: đã mở được trang details");
                }
            });


        }
        return convertView;
    }

    private String ThoiGianChat(long date) {
        String thoigian = "";
        Date datetime = new Date();
        datetime.setTime(date);
        Date currentday = new Date();
        long diffInMillies = Math.abs(datetime.getTime() - currentday.getTime());
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff / (60 * 60 * 24 * 30) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24 * 30)) + context.getString(R.string.thangtruoc);
        } else if (diff / (60 * 60 * 24) > 0) {
            thoigian = Math.round(diff / (60 * 60 * 24)) +context.getString(R.string.ngaytruoc);
        } else if (diff / (60 * 60) > 0) {
            thoigian = Math.round(diff / (60 * 60)) + context.getString(R.string.giotruoc);
        } else if (diff / (60) > 0) {
            thoigian = Math.round(diff / (60)) + context.getString(R.string.phuttruoc);
        } else if (diff > 0) {
            thoigian = Math.round(diff) + context.getString(R.string.giaytruoc);
        } else {
            thoigian =context.getString(R.string.vuaxong);
        }

        return thoigian;
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
        CircleImageView
                img_user_avatar_chat;
        LinearLayout layout_account_message_user;
    }

}
