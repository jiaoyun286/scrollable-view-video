package com.brucetoo.listvideoplay.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brucetoo.listvideoplay.Backable;
import com.nd.sdp.bk.video.R;
import com.nd.sdp.video.tracker.Tracker;

/**
 * Created by Bruce Too
 * On 10/04/2017.
 * At 16:24
 */

public class DetailFragment extends Fragment implements Backable {

    public static final String TAG = "DetailFragment";
    private ImageView mImageCover;
    private TextView mTextDetail;
    private boolean mAnimateRunning;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextDetail = (TextView) view.findViewById(R.id.txt_detail);
        mImageCover = (ImageView) view.findViewById(R.id.view_tracker);
        mImageCover.post(new Runnable() {
            @Override
            public void run() {
                startMoveInside();
            }
        });
    }

    private float transX;
    private float transY;
    private float deltaW;
    private float deltaH;
    private View videoRoot;
    private View videoRootParent;
    private float videoRootTransY;
    private float videoRootTransX;
    /**
     * 将上个页面的trackview保存下来，一遍返回是将视频容器视图在回滚到这个视图的位置
     */
    private View oldTrackerView;

    /**
     * 执行页面切换时，视频容器视图的转场动画，从前一个页面的trackview位置移动到当前页面的的新的trackview位置，中间可能伴随做尺寸变化
     */
    private void startMoveInside() {
        //TODO check NPE
        videoRoot = (Tracker.getViewTracker(getActivity()).getFollowerView());
        oldTrackerView = Tracker.getViewTracker(getActivity()).getTrackerView();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        final int[] loc = new int[2];
//        Tracker.changeTrackView(getActivity(),mImageCover);
        mImageCover.getLocationOnScreen(loc);
        videoRootParent = (View) videoRoot.getParent();
        final Rect rect = new Rect();
        mImageCover.getLocalVisibleRect(rect);
        final float originX = videoRootParent.getTranslationX();
        final float originY = videoRootParent.getTranslationY();
        final int originW = videoRoot.getWidth();
        final int originH = videoRoot.getHeight();
        transX = originX - loc[0];
        transY = originY - loc[1];
        deltaW = originW - mImageCover.getMeasuredWidth();
        deltaH = originH - mImageCover.getMeasuredHeight();
        videoRootTransY = videoRoot.getTranslationY();
        videoRootTransX = videoRoot.getTranslationX();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewAnimator.putOn(videoRootParent).translation(originX - transX * value, originY - transY * value)
                .andPutOn(videoRoot).translation(videoRootTransX * (1 - value) ,videoRootTransY * (1 - value));
                ViewGroup.LayoutParams params = videoRoot.getLayoutParams();
                params.width = (int) (originW - deltaW * value);
                params.height = (int) (originH - deltaH * value);
                videoRoot.requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimateRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //确保动画执行结束时，视频区被移到了新的trackview位置
                Tracker.changeTrackView(getActivity(),mImageCover);
                mAnimateRunning = false;
            }
        });
        animator.setDuration(500);
        animator.start();

        //详情视图转场时的动画
        ViewAnimator.putOn(mTextDetail).animate().translationY(-mTextDetail.getHeight() / 4, 0)
        .alpha(0,1);
    }

    /**
     * 执行退出当前页面动画，视频容器视图要回滚到上个页面的trackview的位置
     * @param listener 动画执行过程的监听
     */
    private void startMoveOutside(final Animator.AnimatorListener listener) {
//        Tracker.changeTrackView(getActivity(),oldTrackerView);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        final int[] loc = new int[2];
        mImageCover.getLocationOnScreen(loc);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewAnimator.putOn(videoRootParent).translation(loc[0] + transX * value, loc[1] + transY * value)
                .andPutOn(videoRoot).translation(videoRootTransX * value,videoRootTransY * value);
                ViewGroup.LayoutParams params = videoRoot.getLayoutParams();
                params.width = (int) (mImageCover.getWidth() + deltaW * value);
                params.height = (int) (mImageCover.getHeight() + deltaH * value);
                videoRoot.requestLayout();
            }
        });
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(animation);
                //滚回到旧的trackview位置
                Tracker.changeTrackView(getActivity(),oldTrackerView);
            }
        });
        animator.start();

        //详情视图转场时的动画
        ViewAnimator.putOn(mTextDetail).animate().translationY(-mTextDetail.getHeight() / 4)
        .alpha(1,0);
    }

    @Override
    public boolean onBackPressed() {
        if(!mAnimateRunning) {
            startMoveOutside(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (getActivity() != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
        return true;
    }
}
