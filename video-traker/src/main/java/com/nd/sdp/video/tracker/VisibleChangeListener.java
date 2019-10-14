package com.nd.sdp.video.tracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:25
 */
public interface VisibleChangeListener {
    /**
     * 视图可见区域大小发生变化时的回调
     * @param visibleRatio  可见比例
     * @param tracker
     */
    void onVisibleChange(float visibleRatio, IViewTracker tracker);
}
