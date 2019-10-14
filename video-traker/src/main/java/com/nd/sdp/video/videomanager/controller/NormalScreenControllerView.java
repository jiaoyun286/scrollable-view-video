package com.nd.sdp.video.videomanager.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.utils.Utils;

import movie.andorid.sdp.nd.com.video_traker.R;

/**
 * @author JiaoYun
 * @date 2019/10/14 22:30
 */
public class NormalScreenControllerView extends BaseControllerView {
    private ProgressBar mProgressBar;

    public NormalScreenControllerView(Context context) {
        super(context);
    }

    public NormalScreenControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalScreenControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {

        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setMax(1000);
        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.video_seek_bar_bg));
        mProgressBar.setProgress(0);
        mProgressBar.setSecondaryProgress(0);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dp2px(getContext(), 3));
        params.addRule(ALIGN_PARENT_BOTTOM);

        addView(mProgressBar, params);
    }

    @Override
    protected void attachWindow(boolean attach) {
        if (!attach) {
            removeCallbacks(mProgressRunnable);
        }
    }

    @Override
    public void setViewTracker(IViewTracker viewTracker) {
        super.setViewTracker(viewTracker);
        viewTracker.muteVideo(true);
        post(mProgressRunnable);
    }

    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            int position = mVideoPlayerView.getCurrentPosition();
            int duration = mVideoPlayerView.getDuration();
            if (duration != 0) {
                long pos = 1000L * position / duration;
                mProgressBar.setProgress((int) pos);
                //max = 1000
                mProgressBar.setSecondaryProgress(mVideoPlayerView.getCurrentBuffer() * 10);
                postDelayed(this, 500);
            }
        }
    };
}
