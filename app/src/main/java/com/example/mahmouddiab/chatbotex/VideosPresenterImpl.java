package com.example.mahmouddiab.chatbotex;

import com.example.mahmouddiab.chatbotex.api.ApiFactory;
import com.example.mahmouddiab.chatbotex.models.YoutubeChannelId;
import com.example.mahmouddiab.chatbotex.models.YoutubeVideos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mahmoud.diab on 4/22/2018.
 */

public class VideosPresenterImpl implements VideosPresenter {

    VideosView videosView;

    public VideosPresenterImpl(VideosView videosView) {
        this.videosView = videosView;
    }

    @Override
    public void getVideos(int maxResults, String pageToken, String playlistId, String key) {
        ApiFactory.createInstance().getYoutubeVideos(maxResults, pageToken, playlistId, key).enqueue(new Callback<YoutubeVideos>() {
            @Override
            public void onResponse(Call<YoutubeVideos> call, Response<YoutubeVideos> response) {
                videosView.getVideos(response.body());
            }

            @Override
            public void onFailure(Call<YoutubeVideos> call, Throwable t) {
                videosView.getVideosError(t.getMessage());
            }
        });
    }

    @Override
    public void onGetChannelId(String key) {
        ApiFactory.createInstance().getChannelId(key).enqueue(new Callback<YoutubeChannelId>() {
            @Override
            public void onResponse(Call<YoutubeChannelId> call, Response<YoutubeChannelId> response) {
                if (response.isSuccessful())
                    videosView.onGetChannelId(response.body());
            }

            @Override
            public void onFailure(Call<YoutubeChannelId> call, Throwable t) {
                videosView.getVideosError(t.getMessage());
            }
        });
    }
}
