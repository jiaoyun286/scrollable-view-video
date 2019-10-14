package com.nd.sdp.video.scrolldetector;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.brucetoo.videoplayer.utils.Reflecter;
import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.utils.ItemChecker;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:36
 */
public class ListScrollDetector implements AbsListView.OnScrollListener,IScrollDetector {

    private static final String TAG = "ListScrollDetector";
    private ListView mListView;
    private AbsListView.OnScrollListener mOriginListener;
    private int mScrollState = IScrollDetector.SCROLL_STATE_IDLE;
    private IViewTracker mViewTracker;

    public ListScrollDetector(ListView listView) {
        this.mListView = listView;
        mOriginListener = Reflecter.on(mListView).get("mOnScrollListener");
        Reflecter.on(mListView).set("mOnScrollListener", this);
    }

    @Override
    public void detach(){
        Reflecter.on(mListView).set("mOnScrollListener", mOriginListener);
    }

    @Override
    public boolean isIdle() {
        return mScrollState == IScrollDetector.SCROLL_STATE_IDLE;
    }

    @Override
    public View getView() {
        return mListView;
    }

    @Override
    public void setTracker(IViewTracker tracker) {
        mViewTracker = tracker;
    }

    @Override
    public void onScrollStateChanged(int scrollState) {
        if (scrollState == IScrollDetector.SCROLL_STATE_IDLE && mViewTracker.getContext() != null) {
            View itemView = ItemChecker.getNextTrackerView(mListView, mViewTracker);
            Log.e(TAG, "onScrollStateChanged: itemView -> " + itemView + " edge -> " + mViewTracker.getEdgeString());
            if (itemView != null) {
                mViewTracker.trackView(itemView).into(this);
                mViewTracker.getFloatLayerView().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        //wrap origin listener
        if (mOriginListener != null) {
            mOriginListener.onScrollStateChanged(absListView, i);
        }

        switch (i) {
            case ListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                mScrollState = IScrollDetector.SCROLL_STATE_TOUCH_SCROLL;
                break;
            case ListView.OnScrollListener.SCROLL_STATE_FLING:
                mScrollState = IScrollDetector.SCROLL_STATE_FLING;
                break;
            case ListView.OnScrollListener.SCROLL_STATE_IDLE:
                mScrollState = IScrollDetector.SCROLL_STATE_IDLE;
                break;
        }

        onScrollStateChanged(mScrollState);
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        //wrap origin listener
        if (mOriginListener != null) {
            mOriginListener.onScroll(absListView, i, i1, i2);
        }
    }
}
