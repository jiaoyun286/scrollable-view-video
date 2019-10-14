package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:06
 */
public class Prepare extends PlayerMessage{

    public Prepare(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.prepare();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.PREPARING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.PREPARED;
    }
}
