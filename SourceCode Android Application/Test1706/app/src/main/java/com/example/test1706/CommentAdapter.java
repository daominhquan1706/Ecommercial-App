package com.example.test1706;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test1706.model.CommentProduct;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends PagerAdapter {
    private Context mContext;
    private List<CommentProduct> commentProductList;

    CommentAdapter(Context context, List<CommentProduct> commentProductList) {
        mContext = context;
        this.commentProductList = commentProductList;
    }

    @Override
    public int getCount() {
        return commentProductList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_comment, container, false);
        CommentProduct commentProduct = commentProductList.get(position);
        CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);

        holder.mUserName.setText(commentProduct.getUserName());
        holder.mContent.setText(commentProduct.getContent());
        holder.mCreateDate.setText(ThoiGian(commentProduct.getCreateDate()));
        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName, mCreateDate, mContent, mRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserName = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateDate = (TextView) itemView.findViewById(R.id.tv_creationdate_nitewatch);
            mContent = (TextView) itemView.findViewById(R.id.tv_content_comment);

        }

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
}
