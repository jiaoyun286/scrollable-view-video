package com.nd.sdp.video.videomanager.controller;

/**
 * @author JiaoYun
 * @date 2019/10/14 22:19
 */
public interface VideoGestureListener {
    /**
     * 单击可滚动视图
     */
    void onSingleTap();

    /**
     * 滑动视频进度条
     *
     * @param seekForward
     */
    void onHorizontalScroll(boolean seekForward);

    /**
     * 竖直方向的手势事件
     *
     * @param percent
     * @param direction
     */
    void onVerticalScroll(float percent, int direction);
}
