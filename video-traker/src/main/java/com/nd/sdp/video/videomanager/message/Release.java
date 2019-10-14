package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:55
 */
public class Release extends PlayerMessage {

    public Release(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.release();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.RELEASING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.RELEASED;
    }
}
