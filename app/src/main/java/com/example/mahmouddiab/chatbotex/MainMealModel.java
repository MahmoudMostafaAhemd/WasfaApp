package com.example.mahmouddiab.chatbotex;

import java.util.ArrayList;

/**
 * Created by mahmoud.diab on 4/4/2018.
 */

public class MainMealModel {
    private ArrayList<String> name;
    private ArrayList<String> img;
    private ArrayList<String> url;
    private ArrayList<String> tags;

    public MainMealModel() {
        name = new ArrayList<>();
        img = new ArrayList<>();
        url = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public void addToNames(String name) {
        this.name.add(name);
    }

    public void addToTags(String tag) {
        this.tags.add(tag);
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void addToImg(String img) {
        this.img.add(img);
    }

    public void addToUrl(String url) {
        this.url.add(url);
    }


    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getImg() {
        return img;
    }

    public ArrayList<String> getUrl() {
        return url;
    }
}
