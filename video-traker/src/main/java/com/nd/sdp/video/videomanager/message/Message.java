package com.nd.sdp.video.videomanager.message;

/**
 * @author JiaoYun
 * @date 2019/10/14 20:54
 */
public interface Message {
    void runMessage();
    void polledFromQueue();
    void messageFinished();
}
