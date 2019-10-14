package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:38
 */
public class Pause extends PlayerMessage {

    public Pause(VideoPlayerView videoView, VideoPlayerManagerCallback callback) {
        super(videoView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.pause();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.PAUSING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.PAUSED;
    }
}
