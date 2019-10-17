package com.nd.sdp.video.videomanager.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.utils.Logger;
import com.nd.sdp.video.videomanager.interfaces.IMediaPlayer;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerListener;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:10
 */
public class DefaultMediaPlayer implements IMediaPlayer,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnPreparedListener{
    private String TAG = "DefaultMediaPlayer";
    private Surface mSurface;

    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final MediaPlayer mMediaPlayer;
    private final AtomicReference<State> mState = new AtomicReference<>();

    private VideoPlayerListener mListener;
    private Context mContext;
    private IViewTracker mViewTracker;

    public DefaultMediaPlayer(Context context, VideoPlayerListener listener) {
        this.mContext = context;
        this.mListener = listener;

        if (Looper.myLooper() != null) {
            throw new RuntimeException("myLooper not null, a bug in some MediaPlayer implementation cause that listeners are not called at all. Please use a thread without Looper");
        }
        mMediaPlayer = new MediaPlayer();

        mState.set(State.IDLE);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
    }


    private final Runnable mOnVideoPreparedMessage = new Runnable() {
        @Override
        public void run() {
            mListener.onVideoPrepared(mViewTracker);
        }
    };

    private final Runnable mOnVideoStartMessage = new Runnable() {
        @Override
        public void run() {
            mListener.onVideoStarted(mViewTracker);
        }
    };

    private final Runnable mOnVideoPauseMessage = new Runnable() {
        @Override
        public void run() {
            mListener.onVideoPaused(mViewTracker);
        }
    };

    private final Runnable mOnVideoStopMessage = new Runnable() {
        @Override
        public void run() {
            mListener.onVideoStopped(mViewTracker);
        }
    };

    private final Runnable mOnVideoResetMessage = new Runnable() {
        @Override
        public void run() {
            Log.d("@@@","--------------5");
            mListener.onVideoReset(mViewTracker);
        }
    };

    private final Runnable mOnVideoReleaseMessage = new Runnable() {
        @Override
        public void run() {
            Log.d("@@@","--------------6");
            mListener.onVideoReleased(mViewTracker);
        }
    };

    @Override
    public void prepare() {
        synchronized (mState) {
            prepareAsync();
        }
    }

    @Override
    public void prepareAsync() {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mState.set(State.PREPARED);
        if (mListener != null) {
            mMainThreadHandler.post(mOnVideoPreparedMessage);
        }
        //prepare 完成 开始播放
        start();
    }

    @Override
    public void setDataSource(@NonNull String url) throws IOException {
        synchronized (mState) {
            mMediaPlayer.setDataSource(mContext, Uri.parse(url));
            mState.set(State.INITIALIZED);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (mListener != null) {
            mListener.onVideoSizeChanged(mViewTracker, width, height);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        synchronized (mState) {
            mState.set(State.PLAYBACK_COMPLETED);
        }

        if (mListener != null) {
            mListener.onVideoCompletion(mViewTracker);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        synchronized (mState) {
            mState.set(State.ERROR);
        }
        if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN || what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            if (mListener != null) {
                mListener.onError(mViewTracker, what, extra);
            }
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mListener != null) {
            mListener.onBufferingUpdate(mViewTracker, percent);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        printInfo(what);
        if (mListener != null) {
            mListener.onInfo(mViewTracker, what);
        }
        return false;
    }

    private void printInfo(int what) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_UNKNOWN:
                Logger.i(TAG, "onInfo, MEDIA_INFO_UNKNOWN");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Logger.i(TAG, "onInfo, MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                Logger.i(TAG, "onInfo, MEDIA_INFO_VIDEO_RENDERING_START");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                Logger.i(TAG, "onInfo, MEDIA_INFO_BUFFERING_START");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                Logger.i(TAG, "onInfo, MEDIA_INFO_BUFFERING_END");
                break;
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Logger.i(TAG, "onInfo, MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Logger.i(TAG, "onInfo, MEDIA_INFO_NOT_SEEKABLE");
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                Logger.i(TAG, "onInfo, MEDIA_INFO_METADATA_UPDATE");
                break;
            case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                Logger.i(TAG, "onInfo, MEDIA_INFO_UNSUPPORTED_SUBTITLE");
                break;
            case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                Logger.i(TAG, "onInfo, MEDIA_INFO_SUBTITLE_TIMED_OUT");
                break;
        }
    }

    @Override
    public void start() {
        Logger.v(TAG, ">> start");

        synchronized (mState) {
            Logger.v(TAG, "start, mState " + mState);
            try {
                mMediaPlayer.start();
            } catch (Exception e) {

            }
            mState.set(State.STARTED);

            if (mListener != null) {
                mMainThreadHandler.post(mOnVideoStartMessage);
            }
        }
        Logger.v(TAG, "<< start");
    }

    @Override
    public void pause() {
        Logger.v(TAG, ">> pause");

        synchronized (mState) {
            Logger.v(TAG, "pause, mState " + mState);
            mMediaPlayer.pause();
            mState.set(State.PAUSED);

            if (mListener != null) {
                mMainThreadHandler.post(mOnVideoPauseMessage);
            }
        }
        Logger.v(TAG, "<< pause");
    }

    @Override
    public void stop() {
        Logger.v(TAG, ">> stop");

        synchronized (mState) {
            Logger.v(TAG, "stop, mState " + mState);

            mMediaPlayer.stop();
            mState.set(State.STOPPED);

            if (mListener != null) {
                mMainThreadHandler.post(mOnVideoStopMessage);
            }
        }
        Logger.v(TAG, "<< stop");
    }

    @Override
    public void reset() {
        Logger.v(TAG, ">> reset , mState " + mState);

        synchronized (mState) {
            mMediaPlayer.reset();
            mState.set(State.IDLE);

            if (mListener != null) {
                mMainThreadHandler.post(mOnVideoResetMessage);
            }
        }
        Logger.v(TAG, "<< reset , mState " + mState);
    }

    @Override
    public void release() {
        Logger.v(TAG, ">> release, mState " + mState);
        synchronized (mState) {
            mMediaPlayer.release();
            mState.set(State.END);

            if (mListener != null) {
                mMainThreadHandler.post(mOnVideoReleaseMessage);
            }
        }
        Logger.v(TAG, "<< release, mState " + mState);
    }

    @Override
    public void clearAll() {
        Logger.v(TAG, ">> clearAll, mState " + mState);
        synchronized (mState) {
            mMediaPlayer.setOnVideoSizeChangedListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.setOnBufferingUpdateListener(null);
            mMediaPlayer.setOnInfoListener(null);
            mMediaPlayer.setOnPreparedListener(null);
        }
        Logger.v(TAG, "<< clearAll, mState " + mState);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        Logger.v(TAG, ">> setSurfaceTexture " + surfaceTexture);
        Logger.v(TAG, "setSurfaceTexture mSurface " + mSurface);

        if (surfaceTexture != null) {
            mSurface = new Surface(surfaceTexture);
            try {
                mMediaPlayer.setSurface(mSurface);
            } catch (IllegalStateException e) {
                //TODO handle exception
            }
        } else {
            mMediaPlayer.setSurface(mSurface);
        }
        Logger.v(TAG, "<< setSurfaceTexture " + surfaceTexture);

    }

    @Override
    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    @Override
    public int getCurrentPosition() {
        int currentPos;
        try {
            currentPos = mMediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
        return currentPos;
    }

    @Override
    public boolean isPlaying() {
        boolean isPlaying;
        try {
            isPlaying = mMediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            return false;
        }
        return isPlaying;
    }

    @Override
    public int getDuration() throws IOException {
        int duration = 0;
        synchronized (mState) {
            duration = mMediaPlayer.getDuration();
        }
        return duration;
    }

    @Override
    public void seekTo(int mis) throws IOException {
        synchronized (mState) {
            State state = mState.get();
            Logger.v(TAG, "seekToPosition, position " + mis + ", mState " + state);
            mMediaPlayer.seekTo(mis);
        }
    }

    @Override
    public void setViewTracker(IViewTracker viewTracker) {
        this.mViewTracker = viewTracker;
    }

    @Override
    public State getCurrentState() {
        synchronized (mState) {
            return mState.get();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode();
    }
}
