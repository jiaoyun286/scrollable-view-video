package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:47
 */
public class Reset extends PlayerMessage {
    public Reset(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.reset();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.RESETTING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.RESET;
    }
}
