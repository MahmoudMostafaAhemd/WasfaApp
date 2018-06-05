package com.example.mahmouddiab.chatbotex.models;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YoutubeChannelId {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("pageInfo")
    @Expose
    private ChannelInfo pageInfo;
    @SerializedName("items")
    @Expose
    private List<ItemChannel> items = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public ChannelInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(ChannelInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ItemChannel> getItems() {
        return items;
    }

    public void setItems(List<ItemChannel> items) {
        this.items = items;
    }

}