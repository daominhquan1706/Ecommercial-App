package com.example.shiper.model;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {
    private int mStartX,mStartY,mEndX,mEndY,mImageIndex,mImageCount;
    private  LinearLayout linearLayout;

    public void setmActivity(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }


    public LinearLayout getLinearLayout() {
        return linearLayout;
    }


    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public MyLinearLayout(Context context) {
        super(context);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // do whatever you want with the event

        // and return true so that children don't receive it
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :

                mStartX = (int)event.getX();
                return true;

            case (MotionEvent.ACTION_MOVE) :

                mEndX = (int)event.getX();
                //move to left
                if((mEndX - mStartX) > 3) {


                }
                //move to right
                if((mEndX - mStartX) < -3) {


                }
                mStartX = (int)event.getX();
                return true;

            case (MotionEvent.ACTION_UP) :
                mEndX = (int)event.getX();

                return true;

            case (MotionEvent.ACTION_CANCEL) :
                return true;

            case (MotionEvent.ACTION_OUTSIDE) :
                return true;

            default :
                return super.onTouchEvent(event);
        }

    }
}
