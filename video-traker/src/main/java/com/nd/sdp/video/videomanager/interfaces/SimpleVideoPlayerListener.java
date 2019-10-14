package com.nd.sdp.video.videomanager.interfaces;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:42
 */
public class SimpleVideoPlayerListener implements VideoPlayerListener {
    @Override
    public void onVideoSizeChanged(IViewTracker viewTracker, int width, int height) {

    }

    @Override
    public void onVideoPrepared(IViewTracker viewTracker) {

    }

    @Override
    public void onVideoCompletion(IViewTracker viewTracker) {

    }

    @Override
    public void onError(IViewTracker viewTracker, int what, int extra) {

    }

    @Override
    public void onBufferingUpdate(IViewTracker viewTracker, int percent) {

    }

    @Override
    public void onVideoStopped(IViewTracker viewTracker) {

    }

    @Override
    public void onVideoReset(IViewTracker viewTracker) {

    }

    @Override
    public void onVideoReleased(IViewTracker viewTracker) {

    }

    @Override
    public void onInfo(IViewTracker viewTracker, int what) {

    }

    @Override
    public void onVideoStarted(IViewTracker viewTracker) {

    }

    @Override
    public void onVideoPaused(IViewTracker viewTracker) {

    }
}
