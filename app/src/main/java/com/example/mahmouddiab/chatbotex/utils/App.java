package com.example.mahmouddiab.chatbotex.utils;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.example.mahmouddiab.chatbotex.R;
import com.kobakei.ratethisapp.RateThisApp;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mahmoud.diab on 4/4/2018.
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DinNextMedium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Paper.init(this);
    }
}
