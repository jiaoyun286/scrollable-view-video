package com.nd.sdp.video.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.OrientationEventListener;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:58
 */
public class OrientationDetector {
    public interface OnOrientationChangedListener {
        void onOrientationChanged(int orientation);
    }

    private OrientationEventListener mEventListener;
    private int mCurrentOrientation;

    public OrientationDetector(Activity activity, final OnOrientationChangedListener listener) {
        mCurrentOrientation = activity.getRequestedOrientation();
        mEventListener = new OrientationEventListener(activity) {
            @Override
            public void onOrientationChanged(int orientation) {
                int newOrientation = mCurrentOrientation;
                //can change number to control accuracy
                if (((orientation >= 0) && (orientation <= 15)) || (orientation > 345)) {
                    newOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                } else if ((orientation > 75) && (orientation <= 105)) {
                    newOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                } else if ((orientation > 165) && (orientation <= 190)) {
                    newOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                } else if ((orientation > 255) && (orientation <= 285)) {
                    newOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }

                if (newOrientation != mCurrentOrientation) {
                    mCurrentOrientation = newOrientation;
                    if (listener != null) {
                        listener.onOrientationChanged(newOrientation);
                    }
                }
            }
        };
    }

    public void enable(boolean enable) {
        if (enable) {
            if (mEventListener.canDetectOrientation()) {
                mEventListener.enable();
            }
        } else {
            mEventListener.disable();
        }
    }
}
