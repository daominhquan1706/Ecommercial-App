package com.example.test1706.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test1706.R;
import com.example.test1706.model.ChatMessage;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Chat_Adapter extends BaseAdapter {

    private List<ChatMessage> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private String currentUser;

    public Chat_Adapter(Context aContext, List<ChatMessage> listData, String currentUser) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.currentUser = currentUser;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_message_item, null);
            holder = new ViewHolder();
            holder.message_text = (TextView) convertView.findViewById(R.id.message_text);
            holder.message_user = (TextView) convertView.findViewById(R.id.message_user);
            holder.layout_item_message_chat = (LinearLayout) convertView.findViewById(R.id.layout_item_message_chat);
            holder.layout_message = (LinearLayout) convertView.findViewById(R.id.layout_message);
            holder.message_time = (TextView) convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatMessage ChatMessage = this.listData.get(position);
        holder.message_text.setText(ChatMessage.getMessageText());
        holder.message_user.setText(ChatMessage.getMessageUser());

        String chattime = ThoiGianChat(ChatMessage.getMessageTime());
        holder.message_time.setText(chattime);

        if (ChatMessage.getMessageUser().equals(currentUser)) {
            holder.layout_message.setGravity(Gravity.END);
            holder.layout_item_message_chat.setBackground(context.getResources().getDrawable(R.drawable.drw_edt_bg_blue));
        } else {
            holder.layout_message.setGravity(Gravity.START);
            holder.layout_item_message_chat.setBackground(context.getResources().getDrawable(R.drawable.drw_edt_bg));
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
            thoigian = Math.round(diff / (60 * 60 * 24)) + context.getString(R.string.ngaytruoc);
        } else if (diff / (60 * 60) > 0) {
            thoigian = Math.round(diff / (60 * 60)) + context.getString(R.string.giotruoc);
        } else if (diff / (60) > 0) {
            thoigian = Math.round(diff / (60)) + context.getString(R.string.phuttruoc);
        } else if (diff > 0) {
            thoigian = Math.round(diff) + context.getString(R.string.giaytruoc);
        } else {
            thoigian = context.getString(R.string.vuaxong);
        }

        return thoigian;
    }

    static class ViewHolder {

        LinearLayout layout_message, layout_item_message_chat;
        TextView message_text;
        TextView message_user;
        TextView message_time;
    }

}