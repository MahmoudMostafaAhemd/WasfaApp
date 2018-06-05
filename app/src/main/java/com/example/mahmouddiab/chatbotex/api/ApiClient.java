package com.example.mahmouddiab.chatbotex.api;

import com.example.mahmouddiab.chatbotex.models.YoutubeChannelId;
import com.example.mahmouddiab.chatbotex.models.YoutubeVideos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mahmoud.diab on 4/21/2018.
 */

public interface ApiClient {

    String baseUrl = "https://www.googleapis.com";

    //GET https://www.googleapis.com/youtube/v3/channels?part=contentDetails&id=UCrE5w3IopeMfvRgmkFKjBAA&key={YOUR_API_KEY}
    @GET("/youtube/v3/channels?part=contentDetails&id=UCrE5w3IopeMfvRgmkFKjBAA")
    Call<YoutubeChannelId> getChannelId(@Query("key") String key);


    @GET("/youtube/v3/playlistItems?part=snippet")
    Call<YoutubeVideos> getYoutubeVideos(@Query("maxResults") int maxResults, @Query("pageToken") String pageToken,
                                         @Query("playlistId") String playlistId, @Query("key") String key);
}
