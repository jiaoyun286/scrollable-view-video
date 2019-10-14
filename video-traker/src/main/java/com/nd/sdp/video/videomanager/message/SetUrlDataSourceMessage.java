package com.nd.sdp.video.videomanager.message;

import com.nd.sdp.video.videomanager.interfaces.VideoPlayerManagerCallback;
import com.nd.sdp.video.videomanager.meta.MetaData;
import com.nd.sdp.video.videomanager.player.VideoPlayerView;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:55
 */
public class SetUrlDataSourceMessage extends SetDataSourceMessage{

    private final MetaData mMetaData;

    public SetUrlDataSourceMessage(VideoPlayerView videoPlayerView, MetaData metaData, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
        mMetaData = metaData;
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        //Get video url
        currentPlayer.setDataSource(mMetaData.getVideoUrl());
    }
}
