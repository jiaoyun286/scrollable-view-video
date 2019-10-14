package com.nd.sdp.video.videomanager.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 22:08
 */
public abstract class BaseControllerView extends RelativeLayout {
    protected IViewTracker mViewTracker;
    protected VideoPlayerView mVideoPlayerView;

    public BaseControllerView(Context context) {
        super(context);
        initView();
    }

    public BaseControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachWindow(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachWindow(false);
    }

    protected abstract void initView();

    protected abstract void attachWindow(boolean attach);

    public void setViewTracker(IViewTracker viewTracker){
        this.mViewTracker = viewTracker;
//        if(mViewTracker.getFloatLayerView() != null) {
        mVideoPlayerView = mViewTracker.getFloatLayerView().getVideoPlayerView();
//        }
    }
}
