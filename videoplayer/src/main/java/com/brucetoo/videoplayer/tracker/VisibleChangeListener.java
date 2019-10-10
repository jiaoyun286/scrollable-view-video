package com.brucetoo.videoplayer.tracker;

/**
 * Created by Bruce Too
 * On 05/04/2017.
 * At 17:47
 */

public interface VisibleChangeListener {

    /**
     * 视图可见区域大小发生变化时的回调
     * @param visibleRatio  可见比例
     * @param tracker
     */
    void onVisibleChange(float visibleRatio, IViewTracker tracker);

}
