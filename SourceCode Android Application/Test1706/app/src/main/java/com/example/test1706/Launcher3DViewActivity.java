package com.example.test1706;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class Launcher3DViewActivity extends AppCompatActivity {
    Context mContext ;
    ImageView m360DegreeImageView;
    int mStartX,mStartY,mEndX,mEndY,mImageIndex,mImageCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageCount=24;
        mContext = this;
        setContentView(R.layout.activity_3d_view);
        m360DegreeImageView = (ImageView)findViewById(R.id.santafe3dview);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :

                mStartX = (int)event.getX();
                mStartY = (int)event.getY();
                return true;

            case (MotionEvent.ACTION_MOVE) :

                mEndX = (int)event.getX();
                mEndY = (int)event.getY();

                if((mEndX - mStartX) > 3) {
                    mImageIndex++;
                    if(mImageIndex > mImageCount )
                        mImageIndex = 0;

                    m360DegreeImageView.setImageLevel(mImageIndex);

                }
                if((mEndX - mStartX) < -3) {
                    mImageIndex--;
                    if(mImageIndex <0)
                        mImageIndex = mImageCount;

                    m360DegreeImageView.setImageLevel(mImageIndex);

                }
                mStartX = (int)event.getX();
                mStartY = (int)event.getY();
                return true;

            case (MotionEvent.ACTION_UP) :
                mEndX = (int)event.getX();
                mEndY = (int)event.getY();

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
