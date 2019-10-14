package com.nd.sdp.video.videomanager.interfaces;

import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.videomanager.PlayerMessageState;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:49
 */
public interface VideoPlayerManagerCallback {
    void setCurrentItem(IViewTracker viewTracker, VideoPlayerView newPlayerView);

    void updateVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
