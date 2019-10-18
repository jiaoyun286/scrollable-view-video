package com.nd.sdp.video.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nd.sdp.video.scrolldetector.IScrollDetector;
import com.nd.sdp.video.utils.Logger;
import com.nd.sdp.video.utils.Utils;
import com.nd.sdp.video.utils.ViewAnimator;
import com.nd.sdp.video.videomanager.controller.IControllerView;
import com.nd.sdp.video.videomanager.meta.MetaData;

import movie.andorid.sdp.nd.com.video_traker.R;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:59
 */
public class ViewTracker implements IViewTracker, ViewTreeObserver.OnScrollChangedListener {
    private static final String TAG = ViewTracker.class.getSimpleName();

    /**
     * 用于获取DecorView的Activity
     */
    protected Activity mContext;

    /**
     * 被追踪的视图的可见性监听
     */
    protected VisibleChangeListener mVisibleChangeListener;

    /**
     * 被追踪的视图，通常是listview，Recylerview中的item视图
     */
    protected View mTrackView;

    /**
     * 播放器视图的容器视图
     */
    protected View mFollowerView;

    /**
     * 播放器容器视图之上的一层视图，用于添加视频控制视图
     */
    protected FrameLayout mVideoTopView;

    /**
     * 播放器容器视图之下的一层视图，用于视频显示区的默认背景
     */
    protected FrameLayout mVideoBottomView;

    /**
     * 一个可竖向滚动的视图，通常是 ListView 或 RecyclerView
     */
    protected View mVerticalScrollView;

    /**
     * 被添加到DecorView中所有视图的根视图，可以添加需要是视图到这个容器内
     */
    protected FloatLayerView mFloatLayerView;

    protected MetaData mMetaData;

    /**
     * 被追踪视图当前的边界
     */
    protected int mCurrentEdge = NONE_EDGE;

    /**
     * 用于检测被追踪视图的滚动状态变化
     */
    protected IScrollDetector mScrollDetector;

    protected IControllerView mControllerView;

    protected boolean mIsAttach;

    /**
     * activity的初始 flag 和 系统 ui 可见性
     */
    protected int mOriginActivityFlag,mOriginSystemUIVisibility;

    /**
     * 播放器视图的原始位置和尺寸
     */
    protected int mOriginX,mOriginY,mOriginWidth,mOriginHeight;


    public ViewTracker(Activity context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null in ViewTracker!");
        }
        this.mContext = context;
    }

    @Override
    public IViewTracker attach() {
        //如果decorView中已经存在以前添加的FloatLayerView，需要先移除，再add新的
        for (int i = 0; i < getDecorView().getChildCount(); i++) {
            View child = getDecorView().getChildAt(i);
            if(child instanceof FloatLayerView){
                getDecorView().removeView(child);
            }
        }
        if (mFloatLayerView == null) {//第一次需要创建
            mFloatLayerView = new FloatLayerView(mContext);
            mFollowerView = mFloatLayerView.getVideoRootView();
            mVideoTopView = mFloatLayerView.getVideoTopView();
            mVideoBottomView = mFloatLayerView.getVideoBottomView();
            restoreActivityFlag();
        }
        if(mFloatLayerView.getParent() == null) {
            //未添加到DecorView
            getDecorView().addView(mFloatLayerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        mIsAttach = true;
        return this;
    }

    @Override
    public IViewTracker detach() {
        if (mTrackView != null) {
            mTrackView.getViewTreeObserver().removeOnScrollChangedListener(this);
        }

        if (mFloatLayerView != null) {
            getDecorView().removeView(mFloatLayerView);
        }
        mIsAttach = false;
        return this;
    }

    @Override
    public IViewTracker hide() {
        if(mFloatLayerView != null){
            mFloatLayerView.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    @Override
    public IViewTracker show() {
        if(mFloatLayerView != null){
            mFloatLayerView.setVisibility(View.VISIBLE);
        }
        return this;
    }

    @Override
    public IViewTracker destroy() {
        detach();
        mVisibleChangeListener = null;
//        mContext = null;//prevent memory leak?
        mScrollDetector.detach();
        mScrollDetector = null;
        return this;
    }

    @Override
    public View getVerticalScrollView() {
        return mVerticalScrollView;
    }

    @Override
    public View getTrackerView() {
        return mTrackView;
    }

    @Override
    public MetaData getMetaData() {
        return mMetaData;
    }

    @Override
    public int getTrackerViewId() {
        return R.id.view_tracker;
    }

    @Override
    public View getFollowerView() {
        return mFollowerView;
    }

    @Override
    public FloatLayerView getFloatLayerView() {
        return mFloatLayerView;
    }

    @Override
    public FrameLayout getVideoTopView() {
        return mVideoTopView;
    }

    @Override
    public FrameLayout getVideoBottomView() {
        return mVideoBottomView;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        if (newConfig.getLayoutDirection() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        if (isFullScreen()) {
            Window window = mContext.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }

            rebindTrackerView(0, 0, Utils.getDeviceWidth(mContext), Utils.getDeviceHeight(mContext));
        } else {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.addFlags(mOriginActivityFlag);
            window.getDecorView().setSystemUiVisibility(mOriginSystemUIVisibility);

            rebindTrackerView(mOriginX, mOriginY, mOriginWidth, mOriginHeight);
        }
    }

    @Override
    public boolean isFullScreen() {
        int orientation = mContext.getRequestedOrientation();
        return orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                || orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
    }

    @Override
    public void toFullScreen() {
        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @Override
    public void toNormalScreen() {
        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void muteVideo(boolean mute) {

    }

    @Override
    public void startVideo() {

    }

    @Override
    public void pauseVideo() {

    }

    @Override
    public IControllerView getControllerView() {
        return mControllerView;
    }

    @Override
    public IViewTracker trackView(@NonNull View trackView) {
        if (mTrackView != null) {//not first
            detach();
            attach();
        }
        this.mTrackView = trackView;
        int id = mTrackView.getId();
        if (id != R.id.view_tracker) {
            throw new IllegalStateException("Tracker view id must be R.id.view_tracker !");
        }
        rebindViewToTracker(mFollowerView, mTrackView);
        trackView.getViewTreeObserver().addOnScrollChangedListener(this);
        return this;
    }

    @Override
    public IViewTracker changeTrackView(View trackView) {
        this.mTrackView.getViewTreeObserver().removeOnScrollChangedListener(this);
        this.mTrackView = trackView;
        int id = mTrackView.getId();
        if (id != R.id.view_tracker) {
            throw new IllegalStateException("Tracker view id must be R.id.view_tracker !");
        }
        rebindViewToTracker(mFollowerView, mTrackView);
        trackView.getViewTreeObserver().addOnScrollChangedListener(this);
        return this;
    }

    @Override
    public IViewTracker into(@NonNull IScrollDetector scrollDetector) {
        this.mScrollDetector = scrollDetector;
        this.mVerticalScrollView = scrollDetector.getView();
        scrollDetector.setTracker(this);
        return this;
    }

    @Override
    public IViewTracker visibleListener(VisibleChangeListener listener) {
        this.mVisibleChangeListener = listener;
        return this;
    }

    @Override
    public IViewTracker controller(IControllerView controllerView) {
        this.mControllerView = controllerView;
        return this;
    }

    @Override
    public boolean isAttach() {
        return mIsAttach;
    }

    @Override
    public int getEdge() {
        return mCurrentEdge;
    }

    @Override
    public String getEdgeString() {
        String edge = "";
        switch (mCurrentEdge) {
            case TOP_EDGE:
                edge = "TOP_EDGE";
                break;
            case BOTTOM_EDGE:
                edge = "BOTTOM_EDGE";
                break;
            case LEFT_EDGE:
                edge = "LEFT_EDGE";
                break;
            case RIGHT_EDGE:
                edge = "RIGHT_EDGE";
                break;
            case NONE_EDGE:
                edge = "NONE_EDGE";
                break;
        }
        return edge;
    }

    @Override
    public void onScrollChanged() {
        //bind to tracker and move..
//        if (mFloatLayerView != null && Config.SHOW_DEBUG_RECT) {// for test
//            mFloatLayerView.testView.setText(getCalculateValueByString(mTrackView));
//        }
        if (!isFullScreen()) {
            moveCurrentView(mVerticalScrollView, mFollowerView, mTrackView);
        }
    }

    private ViewGroup getDecorView() {
        return (ViewGroup) mContext.getWindow().getDecorView();
    }

    private void rebindTrackerView(int x, int y, int width, int height) {
        View parent = (View) mFollowerView.getParent();
        ViewAnimator.putOn(parent).translation(x, y)
                .andPutOn(mFloatLayerView).translation(0, 0);
        mFollowerView.getLayoutParams().width = width;
        mFollowerView.getLayoutParams().height = height;
        mFollowerView.requestLayout();
    }

    private void rebindViewToTracker(View fromView, View toView) {
        int[] locTo = new int[2];
        toView.getLocationOnScreen(locTo);
        View parent = (View) fromView.getParent();
        Logger.i(TAG, "rebindViewToTracker locTo[0] -> " + locTo[0] + " locTo[1] -> " + locTo[1]);
        ViewAnimator.putOn(parent).translation(locTo[0], locTo[1])
                .andPutOn(fromView).translation(0, 0);
//        ViewAnimator.putOn(parent).animate().duration(500).translation(locTo[0],locTo[1]).start(null);
        mOriginX = locTo[0];
        mOriginY = locTo[1];
        mOriginWidth = toView.getWidth();
        mOriginHeight = toView.getHeight();
        Logger.i(TAG, "rebindViewToTracker mOriginX:" + mOriginX + " mOriginY:" + mOriginY + " mOriginWidth:" + mOriginWidth + " mOriginHeight:" + mOriginHeight);
        fromView.getLayoutParams().width = mOriginWidth;
        fromView.getLayoutParams().height = mOriginHeight;
        fromView.requestLayout();
    }


    private int[] locScroll = new int[2];
    private int[] locTo = new int[2];
    private int[] locFrom = new int[2];
    private Rect toViewR = new Rect();
    private Rect scrollViewR = new Rect();

    /**
     * @param scrollParent  列表视图
     * @param fromView 要滚动的视图
     * @param toView 追踪的视图（要滚动到这个视图所在的位置，即listview中被追踪item视图）
     */
    private void moveCurrentView(View scrollParent, View fromView, View toView) {

        scrollParent.getLocationOnScreen(locScroll);

        //获取播放器容器视图的父视图
        View parent = ((View) fromView.getParent());

        //获取视图在屏幕中的坐标位置
        toView.getLocationOnScreen(locTo);
        fromView.getLocationOnScreen(locFrom);

        //获取被追踪视图可见区域Rect对象
        toView.getLocalVisibleRect(toViewR);

        scrollParent.getLocalVisibleRect(scrollViewR);

        Logger.w(TAG, "moveCurrentView: toViewR.top -> " + toViewR.top
                + " toViewR.bottom -> " + toViewR.bottom
                + " toViewR.left -> " + toViewR.left
                + " toViewR.right -> " + toViewR.right
                + " locTo[0] -> " + locTo[0]
                + " locTo[1] -> " + locTo[1]
                + " locFrom[0] -> " + locFrom[0]
                + " locFrom[1] -> " + locFrom[1]);

        if (toViewR.top != 0 || toViewR.bottom != toView.getHeight()
                || toViewR.left != 0 || toViewR.right != toView.getWidth()) { //被追踪的视图正在被滚出屏幕，即部分可见
            Logger.v(TAG, "moveCurrentView: move fromView");
            float moveX = 0;
            float moveY = 0;

            //向上滚动
            if (toViewR.top > 0 && toViewR.top != 0) {
                moveX = -toViewR.left;
                moveY = -toViewR.top;
                //将播放器视图容器视图固定的在可滚动视图的顶部
                ViewAnimator.putOn(parent).translation(locTo[0],
                        locScroll[1] + scrollParent.getPaddingTop());
                mCurrentEdge = TOP_EDGE;
            }

            //向下滚动
            if (toViewR.bottom > 0 && toViewR.bottom != toView.getHeight()) {
                moveY = toView.getHeight() - toViewR.bottom;
                moveX = toView.getWidth() - toViewR.right;
                //将播放器视图容器视图固定的在可滚动视图的底部
                ViewAnimator.putOn(parent).translation(locTo[0],
                        locScroll[1] + scrollViewR.bottom - scrollViewR.top - toView.getMeasuredHeight());
                mCurrentEdge = BOTTOM_EDGE;
            }

            //向左滚动
            if (toViewR.left > 0 && toViewR.left != 0) {
                moveX = -toViewR.left;
                //将播放器视图容器视图固定的在可滚动视图的左边界
                ViewAnimator.putOn(parent).translationX(0);
                mCurrentEdge = LEFT_EDGE;
            }

            //向右滚动
            if (toViewR.right > 0 && toViewR.right != toView.getWidth()) {
                moveX = toView.getWidth() - toViewR.right;
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getVerticalScrollView().getLayoutParams();
                //将播放器视图容器视图固定的在可滚动视图的右边界
                ViewAnimator.putOn(parent).translationX(getVerticalScrollView().getPaddingRight() + getVerticalScrollView().getPaddingLeft()
                        + layoutParams.leftMargin + layoutParams.rightMargin);
                mCurrentEdge = RIGHT_EDGE;
            }

            if (toViewR.left < 0 && toViewR.right < 0) {
                mCurrentEdge = LEFT_EDGE;
            }

            if (toViewR.right >= Utils.getDeviceWidth(mContext)) {
                mCurrentEdge = RIGHT_EDGE;
            }

            //移动视频播放视图容器
            ViewAnimator.putOn(fromView).translation(moveX, moveY);

            //被追踪的item视图，在屏幕内还可见
            if(!isViewOutOfListRect(toViewR,scrollViewR)) {
                float v1 = (toViewR.bottom - toViewR.top) * 1.0f / toView.getHeight();
                float v2 = (toViewR.right - toViewR.left) * 1.0f / toView.getWidth();
                if (mVisibleChangeListener != null) {
                    //竖直方向传高度变化，水平方向传宽度变化
                    mVisibleChangeListener.onVisibleChange(mCurrentEdge == TOP_EDGE || mCurrentEdge == BOTTOM_EDGE ? v1 : v2, this);
                }
            }
        } else {
            Logger.v(TAG, "moveCurrentView: move fromView 的 parent");
            //移动跟视图
            ViewAnimator.putOn(parent).translation(locTo[0], locTo[1])
                    .andPutOn(fromView).translation(0, 0);
        }
    }

    /**
     * 判断追踪是否还可见
     * @param viewR
     * @param scrollViewR
     * @return
     */
    private boolean isViewOutOfListRect(Rect viewR, Rect scrollViewR) {
        return viewR.top > scrollViewR.bottom || // 底部
                viewR.bottom < 0 ||// 顶部
                viewR.left > scrollViewR.right - scrollViewR.left ||//右侧
                viewR.right < 0;//左侧
    }

    private String getCalculateValueByString(View toView) {
        Rect rect = new Rect();
        toView.getLocalVisibleRect(rect);
        StringBuffer buffer = new StringBuffer();
        float v1 = (rect.bottom - rect.top) * 1.0f / toView.getHeight();
        float v2 = (rect.right - rect.left) * 1.0f / toView.getWidth();
        buffer.append("top:").append(rect.top)
                .append(" - ")
                .append("bottom:").append(rect.bottom)
                .append(" \n ")
                .append("left:").append(rect.left)
                .append(" - ")
                .append("right:").append(rect.right)
                .append(" \n ")
                .append("visible:").append(String.format("%.2f", v1 == 1 ? v2 : v1).toString());
        return buffer.toString();
    }

    protected void restoreActivityFlag() {
        Window window = mContext.getWindow();
        mOriginActivityFlag = window.getAttributes().flags;
        mOriginSystemUIVisibility = window.getDecorView().getSystemUiVisibility();
    }

    /**
     * 保持屏幕常亮
     * @param on
     */
    protected void keepScreenOn(boolean on) {
        if (on) {
            mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
