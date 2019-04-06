package com.example.test1706;

import android.widget.BaseAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {

    private List<ChatMessage> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext, List<ChatMessage> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
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
            holder.message_time = (TextView) convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatMessage ChatMessage = this.listData.get(position);
        holder.message_text.setText(ChatMessage.getMessageText());
        holder.message_user.setText(ChatMessage.getMessageUser());
        holder.message_time.setText(String.valueOf(ChatMessage.getMessageTime()));

        return convertView;
    }


    static class ViewHolder {
        TextView message_text;
        TextView message_user;
        TextView message_time;

    }

}