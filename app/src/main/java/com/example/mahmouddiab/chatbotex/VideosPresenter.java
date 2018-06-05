package com.example.mahmouddiab.chatbotex;

/**
 * Created by mahmoud.diab on 4/22/2018.
 */

public interface VideosPresenter {
    void getVideos(int maxResults, String pageToken,
                   String playlistId, String key);

    void onGetChannelId(String key);
}
