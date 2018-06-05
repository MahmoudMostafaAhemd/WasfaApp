package com.example.mahmouddiab.chatbotex.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mahmouddiab.chatbotex.mainPager.AdvicesFragment;
import com.example.mahmouddiab.chatbotex.mainPager.HealthAndDietFragment;
import com.example.mahmouddiab.chatbotex.mainPager.MainFragment;
import com.example.mahmouddiab.chatbotex.mainPager.RecipesFragment;
import com.example.mahmouddiab.chatbotex.mainPager.VideosFragment;

/**
 * Created by mahmoud.diab on 3/18/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private String[] titles;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm, String[] titles) {
        super(fm);
        this.context = context;
        this.titles = titles;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new RecipesFragment();
            case 2:
                return new AdvicesFragment();
            case 3:
                return new HealthAndDietFragment();
            default:
                return new VideosFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 5;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return titles[position];
    }
}
