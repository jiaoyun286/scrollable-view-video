package com.nd.sdp.video.videomanager.controller;

import android.view.View;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 22:16
 */
public class DefaultControllerView implements IControllerView {
    @Override
    public View normalScreenController(IViewTracker tracker) {
//        tracker.muteVideo(true);
        return new NormalScreenControllerView(tracker.getContext());
    }

    @Override
    public View detailScreenController(IViewTracker tracker) {
        return fullScreenController(tracker);
    }

    @Override
    public View fullScreenController(IViewTracker tracker) {
//        tracker.muteVideo(false);
        return new FullScreenControllerView(tracker.getContext());
    }

    @Override
    public View loadingController(IViewTracker tracker) {
        return new LoadingControllerView(tracker.getContext());
    }

    @Override
    public View anotherController(IViewTracker tracker) {
        return null;
    }

    @Override
    public boolean muteVideo() {
        return false;
    }

    @Override
    public boolean enableAutoRotation() {
        return true;
    }
}
