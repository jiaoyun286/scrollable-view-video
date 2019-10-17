package com.nd.sdp.video.scrolldetector;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.utils.ItemChecker;
import com.nd.sdp.video.utils.Reflecter;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:33
 */
public class RecyclerScrollDetector extends RecyclerView.OnScrollListener implements IScrollDetector {
    private static final String TAG = "RecyclerScrollDetector";
    private RecyclerView mRecyclerView;
    private int mScrollState = IScrollDetector.SCROLL_STATE_IDLE;
    private IViewTracker mViewTracker;
    private RecyclerView.OnScrollListener mOriginListener;

    public RecyclerScrollDetector(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        mOriginListener = Reflecter.on(mRecyclerView).get("mScrollListener");
        Reflecter.on(mRecyclerView).set("mScrollListener", this);
    }

    @Override
    public View getView() {
        return mRecyclerView;
    }


    @Override
    public void setTracker(IViewTracker tracker) {
        mViewTracker = tracker;
    }

    @Override
    public void onScrollStateChanged(int scrollState) {
        if (scrollState == IScrollDetector.SCROLL_STATE_IDLE && mViewTracker.getContext() != null) {
            View itemView = ItemChecker.getNextTrackerView(mRecyclerView, mViewTracker);
            Log.e(TAG, "onScrollStateChanged: itemView -> " + itemView + " edge -> " + mViewTracker.getEdgeString());
            if (itemView != null) {
                mViewTracker.trackView(itemView).into(this);
                mViewTracker.getFloatLayerView().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void detach() {
        Reflecter.on(mRecyclerView).set("mScrollListener", mOriginListener);
    }

    @Override
    public boolean isIdle() {
        return mScrollState == IScrollDetector.SCROLL_STATE_IDLE;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                mScrollState = IScrollDetector.SCROLL_STATE_TOUCH_SCROLL;
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                mScrollState = IScrollDetector.SCROLL_STATE_FLING;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                mScrollState = IScrollDetector.SCROLL_STATE_IDLE;
                break;
        }

        onScrollStateChanged(mScrollState);
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mOriginListener != null) {
            mOriginListener.onScrolled(recyclerView, dx,dy);
        }
    }
}
