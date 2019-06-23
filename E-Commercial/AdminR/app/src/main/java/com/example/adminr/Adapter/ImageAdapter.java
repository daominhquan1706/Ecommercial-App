package com.example.adminr.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.adminr.R;

import org.w3c.dom.Comment;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private int[]  mImageIds= new int[] {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

    public ImageAdapter(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageview = new ImageView(mContext);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setImageResource(mImageIds[position]);
        container.addView(imageview,0);
        return imageview;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
