package com.nd.sdp.video.videomanager.player;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @author JiaoYun
 * @date 2019/10/14 22:34
 */
public class RatioImageView extends AppCompatImageView {
    private int mWidth;
    private int mHeight;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRatio(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidth > 0 && mHeight > 0) {
            float ratio = (float) mWidth / mHeight;
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (width > 0) {
                height = (int) (width / ratio);
            } else if (height > 0) {
                width = (int) (height * ratio);
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
