package com.example.mahmouddiab.chatbotex.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("high")
    @Expose
    private High high;

    @SerializedName("maxres")
    @Expose
    private High maxres;

    public High getMaxres() {
        return maxres;
    }

    public void setMaxres(High maxres) {
        this.maxres = maxres;
    }

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }


}