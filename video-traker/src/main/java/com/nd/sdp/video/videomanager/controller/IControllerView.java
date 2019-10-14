package com.nd.sdp.video.videomanager.controller;

import android.view.View;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:08
 */
public interface IControllerView {

    View normalScreenController(IViewTracker tracker);


    View detailScreenController(IViewTracker tracker);


    View fullScreenController(IViewTracker tracker);


    View loadingController(IViewTracker tracker);


    View anotherController(IViewTracker tracker);


    boolean muteVideo();

    boolean enableAutoRotation();
}
