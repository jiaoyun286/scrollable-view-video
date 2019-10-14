package com.nd.sdp.video.scrolldetector;

import android.view.View;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:24
 */
public interface IScrollDetector {
    int SCROLL_STATE_IDLE = 0;

    int SCROLL_STATE_TOUCH_SCROLL = 1;

    int SCROLL_STATE_FLING = 2;

    View getView();

    void setTracker(IViewTracker tracker);

    void onScrollStateChanged(int scrollState);

    void detach();

    boolean isIdle();
}
