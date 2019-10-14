package com.nd.sdp.video.videomanager.interfaces;

import com.nd.sdp.video.tracker.IViewTracker;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 21:50
 */
public interface VideoPlayerManager<T extends IViewTracker> {
    /**
     * @param viewTracker
     * @param videoPlayerView
     */
    void playNewVideo(T viewTracker, VideoPlayerView videoPlayerView);


    void stopAnyPlayback();


    void resetMediaPlayer();

    void startVideo();

    void pauseVideo();
}
