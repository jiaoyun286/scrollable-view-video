package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.utils.Logger;
import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:26
 */
public abstract class PlayerMessage implements Message {
    private static final String TAG = PlayerMessage.class.getSimpleName();
    private final VideoPlayerView mCurrentPlayer;
    private final VideoPlayerManagerCallback mCallback;

    public PlayerMessage(VideoPlayerView currentPlayer, VideoPlayerManagerCallback callback) {
        mCurrentPlayer = currentPlayer;
        mCallback = callback;
    }

    protected final PlayerMessageState getCurrentState() {
        return mCallback.getCurrentPlayerState();
    }

    @Override
    public final void polledFromQueue() {
        mCallback.updateVideoPlayerState(mCurrentPlayer, stateBefore());
    }

    @Override
    public final void messageFinished() {
        mCallback.updateVideoPlayerState(mCurrentPlayer, stateAfter());
    }

    public final void runMessage() {
        Logger.v(TAG, ">> runMessage, " + getClass().getSimpleName());
        performAction(mCurrentPlayer);
        Logger.v(TAG, "<< runMessage, " + getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    protected abstract void performAction(VideoPlayerView currentPlayer);

    protected abstract PlayerMessageState stateBefore();

    protected abstract PlayerMessageState stateAfter();
}
