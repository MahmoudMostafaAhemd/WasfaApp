package com.example.mahmouddiab.chatbotex;

import com.example.mahmouddiab.chatbotex.models.YoutubeChannelId;
import com.example.mahmouddiab.chatbotex.models.YoutubeVideos;

/**
 * Created by mahmoud.diab on 4/22/2018.
 */

public interface VideosView {
    void getVideos(YoutubeVideos youtubeVideos);

    void getVideosError(String error);

    void onGetChannelId(YoutubeChannelId youtubeChannelId);
}
