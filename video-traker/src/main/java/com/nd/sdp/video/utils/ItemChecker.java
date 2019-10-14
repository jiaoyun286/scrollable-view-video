package com.nd.sdp.video.utils;

import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.nd.sdp.video.tracker.IViewTracker;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:37
 */
public class ItemChecker {
    private static final String TAG = "ItemChecker";

    /**
     * 获取下一个完全可见的TrackView
     * 获取策略根据followView位移时触边方向确定，顶部触边，则从上向下第一个完全可见item为TrackView，
     * 底部触边，则从下向上第一个完全可见item未TrackView
     * @param listView current listView
     */
    public static View getNextTrackerView(ListView listView, IViewTracker tracker) {

        if (listView == null) {
            return null;
        }
        int childCount = listView.getChildCount();

        int edge = tracker.getEdge();
        Log.d(TAG,"edge = " + edge);
        switch (edge) {
            case IViewTracker.TOP_EDGE:
            case IViewTracker.RIGHT_EDGE:
            case IViewTracker.LEFT_EDGE:
                for (int i = 0; i < childCount; i++) {
                    View itemView = listView.getChildAt(i);
                    if (itemView == null) {
                        return null;
                    }
                    View container = itemView.findViewById(tracker.getTrackerViewId());
                    if (container == null) {
                        continue;
                    }
                    //only care about cover rect, not itemView
                    Rect rect = new Rect();
                    container.getLocalVisibleRect(rect);
                    Log.e(TAG, "getNextTrackerView Bottom: item = " + i
                            + " rectLocal : " + rect);
                    if (rect.bottom >= 0 && rect.left == 0 && rect.top == 0) {
                        if (rect.bottom - rect.top == container.getHeight()) {
                            return container;
                        }
                    }
                }
                break;
            case IViewTracker.BOTTOM_EDGE:
                for (int i = childCount - 1; i >= 0; i--) {
                    View itemView = listView.getChildAt(i);
                    if (itemView == null) {
                        return null;
                    }
                    View container = itemView.findViewById(tracker.getTrackerViewId());
                    if (container == null) {
                        continue;
                    }
                    //only care about cover rect, not itemView
                    Rect rect = new Rect();
                    container.getLocalVisibleRect(rect);
                    Log.e(TAG, "getNextTrackerView Bottom: item = " + i
                            + " rectLocal : " + rect);
                    if (rect.left == 0 && rect.top == 0) {
                        if (rect.bottom - rect.top == container.getHeight()) {
                            return container;
                        }
                    }
                }
                break;
        }
        return null;
    }

    /**
     * 获取ListView中无安全可见的item的高度
     *
     * @param listView current listView
     * @param coverId
     * @return item view
     */
    public static View getRelativeMostVisibleItemView(ListView listView, @IdRes int coverId) {
        if (listView == null) {
            return null;
        }
        int childCount = listView.getChildCount();
        int mostVisibleItemIndex = -1;
        int maxVisibleHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View itemView = listView.getChildAt(i);
            if (itemView == null) {
                return null;
            }
            View container = itemView.findViewById(coverId);
            if (container == null) {
                continue;
            }
            //only care about cover rect, not itemView
            Rect rect = new Rect();
            container.getLocalVisibleRect(rect);
            if (rect.bottom >= 0 && rect.left == 0 && rect.top == 0) {
                int visibleHeight = rect.bottom - rect.top;
                if (maxVisibleHeight < visibleHeight) {
                    maxVisibleHeight = visibleHeight;
                    mostVisibleItemIndex = i;
                }
            }
        }
        return listView.getChildAt(mostVisibleItemIndex);
    }
}
