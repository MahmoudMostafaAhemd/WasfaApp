package com.example.mahmouddiab.chatbotex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.adapters.SimpleFragmentPagerAdapter;
import com.example.mahmouddiab.chatbotex.base.BaseActivity;
import com.example.mahmouddiab.chatbotex.details.ActivityDetails;
import com.example.mahmouddiab.chatbotex.details.SearchActivity;
import com.example.mahmouddiab.chatbotex.history.HistoryActivity;
import com.example.mahmouddiab.chatbotex.views.RtlViewPager;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kobakei.ratethisapp.RateThisApp;
import com.mancj.materialsearchbar.MaterialSearchBar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener {


    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindArray(R.array.tabs)
    String[] tabsName;
    @BindView(R.id.viewPager)
    RtlViewPager viewPager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;


    //doc.select("div[class=col-xl-16]").select("div[class=col-xl-4 col-lg-4  col-md-8 col-sm-8 col-xs-16 img-blo widget_padding]")recentMeals
//doc.select("div[class=col-xl-16]").select("div[class=col-xl-4  col-lg-4 img-blo col-md-8  col-sm-8 col-xs-16 ]")//أصناف و مطابخ
    //doc.select("div[class=col-xl-16]").select("div[class=col-xl-4 col-lg-4  col-md-8 col-sm-8 col-xs-16 img-blo widget_padding]").select("a").attr("href") itemUrl
    //doc.select("div[class=col-xl-16]").select("div[class=col-xl-4 col-lg-4  col-md-8 col-sm-8 col-xs-16 img-blo widget_padding]").select("img").attr("src")imgUrl
    //doc.select("div[class=col-xl-16]").select("div[class=col-xl-4 col-lg-4  col-md-8 col-sm-8 col-xs-16 img-blo widget_padding]").select("img").attr("alt")mealName
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MobileAds.initialize(this, "ca-app-pub-1465589563932518~8447946529");
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
        config.setTitle(R.string.rate_title);
        config.setMessage(R.string.rate_msg);
        config.setYesButtonText(R.string.yes);
        config.setNoButtonText(R.string.no);
        config.setCancelButtonText(R.string.later);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/recipes");
        for (String aTabsName : tabsName) {
            tabLayout.addTab(tabLayout.newTab().setText(aTabsName));
        }

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager(), tabsName);
        viewPager.setAdapter(adapter);
        searchBar.setOnSearchActionListener(this);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
//        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        if (getSupportFragmentManager().findFragmentByTag("MainFragment") != null) {
//            ft.show(getSupportFragmentManager().findFragmentByTag("MainFragment")).commit();
//        } else {
//            ft.add(R.id.container, new MainFragment(), "MainFragment").commit();
//        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (getIntent() != null && getIntent().hasExtra("image")) {
            String image = getIntent().getStringExtra("image");
            String url = getIntent().getStringExtra("url");

            ActivityDetails.start(this, image, url, false, false);
        }

    }


    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

        SearchActivity.start(this, text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                title.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.history)
    void onHistoryClicked() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Aesthetic.resume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        Aesthetic.pause(this);
//        super.onPause();
//    }

}
