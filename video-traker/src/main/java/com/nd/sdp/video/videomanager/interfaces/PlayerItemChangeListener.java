package com.nd.sdp.video.videomanager.interfaces;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:43
 */
public interface PlayerItemChangeListener {

    /**
     *
     * @param viewTracker
     */
    void onPlayerItemChanged(IViewTracker viewTracker);
}
