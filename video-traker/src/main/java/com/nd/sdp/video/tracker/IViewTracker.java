package com.nd.sdp.video.tracker;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.FrameLayout;

import com.nd.sdp.video.scrolldetector.IScrollDetector;
import com.nd.sdp.video.videomanager.controller.IControllerView;
import com.nd.sdp.video.videomanager.meta.MetaData;

/**
 * 绑定一个视图（TrackView）的矩形区域到另外一个关心他的位置的视图（FollowView），并且追踪他的位置变化
 * @author JiaoYun
 * @date 2019/10/14 20:26
 */
public interface IViewTracker {
    //FollowView滚动后的触的边界
    int NONE_EDGE = 0;
    int TOP_EDGE = 1;
    int BOTTOM_EDGE = 2;
    int LEFT_EDGE = 3;
    int RIGHT_EDGE = 4;

    /**
     * 添加一个FollowView到DecorView
     * @return
     */
    IViewTracker attach();

    /**
     * 将FollowView从DecorView移除
     * @return
     */
    IViewTracker detach();
    /**
     * 隐藏当前 {@link #getFloatLayerView()},不是删除,并且暂停视频
     * 如果存在
     */
    IViewTracker hide();

    /**
     * 现实当前 {@link #getFloatLayerView()},并且开始播放，如果有正在播放的视频
     */
    IViewTracker show();

    /**
     * 释放不需要资源，通常在Actvity被销毁是调用
     */
    IViewTracker destroy();

    /**
     * Offer a tracker view for follower view to track
     * Need Detach the old one and attach new one
     * @param trackView the view that be tracked scroll
     */
    IViewTracker trackView(View trackView);

    /**
     * Just simple bind current {@link #getFollowerView()} to new trackerView
     * Only {@link #getTrackerView()} changed. don't re-attach to screen
     * @param trackView new tracker view
     */
    IViewTracker changeTrackView(View trackView);

    /**
     * Bind a {@link IScrollDetector} of tracker view,in case we
     * can watch scroll state change to make something happen
     * @param scrollDetector tracker view's scroll detector
     */
    IViewTracker into(IScrollDetector scrollDetector);

    /**
     * Observe the visible rect change of tracker view when
     * tracker view scroll position changed
     * @param listener rect change listener
     */
    IViewTracker visibleListener(VisibleChangeListener listener);

    IViewTracker controller(IControllerView controllerView);

    /**
     * 检查follower view 是否已经添加到DecorView
     */
    boolean isAttach();

    /**
     * 获取Tracker的当前滚动边界
     */
    int getEdge();

    /**
     * 格式化化edge，用于打印可读的log
     */
    String getEdgeString();

    /**
     * 获取TrackView所在的可滚动视图
     * 比如 {@link android.widget.ListView},{@link android.support.v7.widget.RecyclerView}
     */
    View getVerticalScrollView();

    /**
     * 获取被追踪的视图，视频显示区需要跟随他位置变化而滚动
     */
    View getTrackerView();

    /**
     * 获取绑定的视频数据
     */
    MetaData getMetaData();


    int getTrackerViewId();

    /**
     * 获取需要绑定到 {@link #getTrackerView()},并且跟着滚动.
     */
    View getFollowerView();

    /**
     * 获取根视图 {@link #getFollowerView()}, 需要添加到
     * DecorView {@link android.view.Window#ID_ANDROID_CONTENT}的视图
     */
    FloatLayerView getFloatLayerView();

    FrameLayout getVideoTopView();

    FrameLayout getVideoBottomView();


    Context getContext();

    /**
     * 屏幕方向改变是会回到
     * @param newConfig 新的配置参数
     */
    void onConfigurationChanged(Configuration newConfig);

    /**
     * 判断是否是横屏
     */
    boolean isFullScreen();

    /**
     * 切换Activity到横屏
     */
    void toFullScreen();

    /**
     * 切换Activity到竖屏
     */
    void toNormalScreen();

    void muteVideo(boolean mute);

    void startVideo();

    void pauseVideo();

    IControllerView getControllerView();
}
