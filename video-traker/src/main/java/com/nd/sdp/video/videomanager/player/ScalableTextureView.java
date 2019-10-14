package com.nd.sdp.video.videomanager.player;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

import com.nd.sdp.video.utils.Logger;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:58
 */
public abstract class ScalableTextureView extends TextureView {
    private static final boolean SHOW_LOGS = true;
    private static final String TAG = ScalableTextureView.class.getSimpleName();

    private int mContentWidth;
    private int mContentHeight;

    private float mPivotPointX = 0f;
    private float mPivotPointY = 0f;

    private float mContentScaleX = 1f;
    private float mContentScaleY = 1f;

    private float mContentRotation = 0f;

    private int mContentX = 0;
    private int mContentY = 0;

    private final Matrix mTransformMatrix = new Matrix();

    private ScaleType mScaleType = ScaleType.FILL;

    public enum ScaleType {
        CENTER_CROP, TOP, BOTTOM, FILL
    }

    public ScalableTextureView(Context context) {
        super(context);
    }

    public ScalableTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScalableTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScaleType(ScaleType scaleType) {
        mScaleType = scaleType;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (SHOW_LOGS)
            Logger.v(TAG, "onMeasure, mContentWidth " + mContentWidth + ", mContentHeight " + mContentHeight);

        if (mContentWidth > 0 && mContentHeight > 0) {
            updateTextureViewSize();
        }
    }

    public void updateTextureViewSize() {
        if (SHOW_LOGS) Logger.d(TAG, ">> updateTextureViewSize");
        if (mContentWidth < 0 || mContentHeight < 0) {
            return;
        }

        float viewWidth = getMeasuredWidth();
        float viewHeight = getMeasuredHeight();

        float contentWidth = mContentWidth;
        float contentHeight = mContentHeight;

        if (SHOW_LOGS) {
            Logger.v(TAG, "updateTextureViewSize, mContentWidth " + mContentWidth + ", mContentHeight " + mContentHeight + ", mScaleType " + mScaleType);
            Logger.v(TAG, "updateTextureViewSize, viewWidth " + viewWidth + ", viewHeight " + viewHeight);
        }

        float scaleX = 1.0f;
        float scaleY = 1.0f;

        switch (mScaleType) {
            case FILL:
                if (viewWidth / viewHeight > contentWidth / contentHeight) {
                    //播放器高度大于当前视图
                    if (contentWidth / contentHeight >= 1.5) { // 12:7 16:9,忽略一些不常见的比例 720/564
                        scaleY = (contentHeight * viewWidth) / (viewHeight * contentWidth);
                    }
                } else {
                    //播放器高度大于当前视图
                    scaleX = (viewHeight * contentWidth) / (viewWidth * contentHeight);
                }
                break;
            case BOTTOM:
            case CENTER_CROP:
            case TOP:
                if (contentWidth > viewWidth && contentHeight > viewHeight) {
                    scaleX = contentWidth / viewWidth;
                    scaleY = contentHeight / viewHeight;
                } else if (contentWidth < viewWidth && contentHeight < viewHeight) {
                    scaleY = viewWidth / contentWidth;
                    scaleX = viewHeight / contentHeight;
                } else if (viewWidth > contentWidth) {
                    scaleY = (viewWidth / contentWidth) / (viewHeight / contentHeight);
                } else if (viewHeight > contentHeight) {
                    scaleX = (viewHeight / contentHeight) / (viewWidth / contentWidth);
                }
                break;
        }

        if (SHOW_LOGS) {
            Logger.v(TAG, "updateTextureViewSize, scaleX " + scaleX + ", scaleY " + scaleY);
        }

        // Calculate pivot points, in our case crop from center
        float pivotPointX;
        float pivotPointY;

        switch (mScaleType) {
            case TOP:
                pivotPointX = 0;
                pivotPointY = 0;
                break;
            case BOTTOM:
                pivotPointX = viewWidth;
                pivotPointY = viewHeight;
                break;
            case CENTER_CROP:
                pivotPointX = viewWidth / 2;
                pivotPointY = viewHeight / 2;
                break;
            case FILL:
                pivotPointX = viewWidth / 2;
                pivotPointY = viewHeight / 2;
                break;
            default:
                throw new IllegalStateException("pivotPointX, pivotPointY for ScaleType " + mScaleType + " are not defined");
        }

        if (SHOW_LOGS)
            Logger.v(TAG, "updateTextureViewSize, pivotPointX " + pivotPointX + ", pivotPointY " + pivotPointY);

        float fitCoef = 1;
        switch (mScaleType) {
            case FILL:
                break;
            case BOTTOM:
            case CENTER_CROP:
            case TOP:
                if (mContentHeight > mContentWidth) { //竖向 video
                    fitCoef = viewWidth / (viewWidth * scaleX);
                } else { //横向 video
                    fitCoef = viewHeight / (viewHeight * scaleY);
                }
                break;
        }

        mContentScaleX = fitCoef * scaleX;
        mContentScaleY = fitCoef * scaleY;

        mPivotPointX = pivotPointX;
        mPivotPointY = pivotPointY;

        updateMatrixScaleRotate();
        if (SHOW_LOGS) Logger.d(TAG, "<< updateTextureViewSize");
    }

    private void updateMatrixScaleRotate() {
        if (SHOW_LOGS)
            Logger.d(TAG, ">> updateMatrixScaleRotate, mContentRotation " + mContentRotation + ", mPivotPointX " + mPivotPointX + ", mPivotPointY " + mPivotPointY);

        mTransformMatrix.reset();
        mTransformMatrix.setScale(mContentScaleX, mContentScaleY, mPivotPointX, mPivotPointY);
        mTransformMatrix.postRotate(mContentRotation, mPivotPointX, mPivotPointY);
        setTransform(mTransformMatrix);
        if (SHOW_LOGS)
            Logger.d(TAG, "<< updateMatrixScaleRotate, mContentRotation " + mContentRotation + ", mPivotPointX " + mPivotPointX + ", mPivotPointY " + mPivotPointY);
    }

    private void updateMatrixTranslate() {
        if (SHOW_LOGS) {
            Logger.d(TAG, "updateMatrixTranslate, mContentX " + mContentX + ", mContentY " + mContentY);
        }

        float scaleX = mContentScaleX;
        float scaleY = mContentScaleY;

        mTransformMatrix.reset();
        mTransformMatrix.setScale(scaleX, scaleY, mPivotPointX, mPivotPointY);
        mTransformMatrix.postTranslate(mContentX, mContentY);
        setTransform(mTransformMatrix);
    }

    @Override
    public void setRotation(float degrees) {
        if (SHOW_LOGS)
            Logger.d(TAG, "setRotation, degrees " + degrees + ", mPivotPointX " + mPivotPointX + ", mPivotPointY " + mPivotPointY);

        mContentRotation = degrees;

        updateMatrixScaleRotate();
    }

    @Override
    public float getRotation() {
        return mContentRotation;
    }

    @Override
    public void setPivotX(float pivotX) {
        if (SHOW_LOGS) Logger.d(TAG, "setPivotX, pivotX " + pivotX);

        mPivotPointX = pivotX;
    }

    @Override
    public void setPivotY(float pivotY) {
        if (SHOW_LOGS) Logger.d(TAG, "setPivotY, pivotY " + pivotY);

        mPivotPointY = pivotY;
    }

    @Override
    public float getPivotX() {
        return mPivotPointX;
    }

    @Override
    public float getPivotY() {
        return mPivotPointY;
    }


    /**
     * 设置中心点的X轴坐标
     *
     * @param x
     */
    public final void setContentX(float x) {
        mContentX = (int) x - (getMeasuredWidth() - getScaledContentWidth()) / 2;
        updateMatrixTranslate();
    }

    /**
     * 设置中心点的Y轴坐标
     *
     * @param y
     */
    public final void setContentY(float y) {
        mContentY = (int) y - (getMeasuredHeight() - getScaledContentHeight()) / 2;
        updateMatrixTranslate();
    }

    protected final float getContentX() {
        return mContentX;
    }

    protected final float getContentY() {
        return mContentY;
    }

    /**
     * 移动TextureView的content到其中心点
     */
    public void centralizeContent() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int scaledContentWidth = getScaledContentWidth();
        int scaledContentHeight = getScaledContentHeight();

        if (SHOW_LOGS)
            Logger.d(TAG, "centralizeContent, measuredWidth " + measuredWidth + ", measuredHeight " + measuredHeight + ", scaledContentWidth " + scaledContentWidth + ", scaledContentHeight " + scaledContentHeight);

        mContentX = 0;
        mContentY = 0;

        if (SHOW_LOGS)
            Logger.d(TAG, "centerVideo, mContentX " + mContentX + ", mContentY " + mContentY);

        updateMatrixScaleRotate();
    }

    public Integer getScaledContentWidth() {
        return (int) (mContentScaleX * getMeasuredWidth());
    }

    public Integer getScaledContentHeight() {
        return (int) (mContentScaleY * getMeasuredHeight());
    }

    public void refreshSurfaceTexture(int contentWidth, int contentHeight) {
        this.mContentWidth = contentWidth;
        this.mContentHeight = contentHeight;
        updateTextureViewSize();
    }
}
