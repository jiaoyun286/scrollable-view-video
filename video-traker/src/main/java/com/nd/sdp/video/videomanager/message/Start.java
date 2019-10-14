package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;


/**
 * @author JiaoYun
 * @date 2019/10/14 21:56
 */
public class Start extends PlayerMessage {

    public Start(VideoPlayerView videoView, VideoPlayerManagerCallback callback) {
        super(videoView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.start();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.STARTING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.STARTED;
    }
}
