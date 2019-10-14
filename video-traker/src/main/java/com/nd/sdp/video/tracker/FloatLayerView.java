package com.nd.sdp.video.tracker;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:38
 */
public class FloatLayerView extends FrameLayout {

    private FrameLayout mVideoBottomView;
    private VideoPlayerView mVideoPlayerView;
    private FrameLayout mVideoTopView;

    public FloatLayerView(@NonNull Context context) {
        super(context);
        init();
    }

    public FloatLayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatLayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mVideoBottomView = new FrameLayout(getContext());
        mVideoTopView = new FrameLayout(getContext());
        mVideoPlayerView = new VideoPlayerView(getContext());

        FrameLayout videoRoot = new FrameLayout(getContext());
        videoRoot.addView(mVideoBottomView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoRoot.addView(mVideoPlayerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoRoot.addView(mVideoTopView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        FrameLayout rootLayout = new FrameLayout(getContext());
        rootLayout.addView(videoRoot,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(rootLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public View getVideoRootView() {
        return (View) mVideoPlayerView.getParent();
    }

    public FrameLayout getVideoBottomView(){
        return mVideoBottomView;
    }

    public FrameLayout getVideoTopView(){
        return mVideoTopView;
    }

    public VideoPlayerView getVideoPlayerView(){
        return mVideoPlayerView;
    }
}
