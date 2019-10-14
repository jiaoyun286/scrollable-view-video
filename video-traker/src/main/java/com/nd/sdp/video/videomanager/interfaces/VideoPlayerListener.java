package com.nd.sdp.video.videomanager.interfaces;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:04
 */
public interface VideoPlayerListener {
    void onVideoSizeChanged(IViewTracker viewTracker, int width, int height);

    void onVideoPrepared(IViewTracker viewTracker);

    void onVideoCompletion(IViewTracker viewTracker);

    void onError(IViewTracker viewTracker, int what, int extra);

    void onBufferingUpdate(IViewTracker viewTracker, int percent);

    void onInfo(IViewTracker viewTracker, int what);

    void onVideoStarted(IViewTracker viewTracker);

    void onVideoPaused(IViewTracker viewTracker);

    void onVideoStopped(IViewTracker viewTracker);

    void onVideoReset(IViewTracker viewTracker);

    void onVideoReleased(IViewTracker viewTracker);
}
