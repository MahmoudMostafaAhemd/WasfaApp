package com.example.mahmouddiab.chatbotex.details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.adapters.CategoriesAdapter;
import com.example.mahmouddiab.chatbotex.base.BaseActivity;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MainMealModel mainMealModel;
    private CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        MyItemDecoration itemDecoration = new MyItemDecoration(this, R.dimen._5sdp);

        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mainMealModel = new MainMealModel();
        categoriesAdapter = new CategoriesAdapter(this, true, mainMealModel);
        recyclerView.setAdapter(categoriesAdapter);
        callHomePage();
    }

    private void callHomePage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.ma7shy.com").get();
                    Elements elements1 = doc.select("div[class=col-xl-4  col-lg-4 img-blo col-md-8  col-sm-8 col-xs-16 ]");
                    for (int i = 0; i < elements1.size(); i++) {
                        mainMealModel.addToUrl(elements1.get(i).select("a").attr("href"));
                        mainMealModel.addToImg(elements1.get(i).select("img").attr("src"));
                        mainMealModel.addToNames(elements1.get(i).select("img").attr("alt"));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                            categoriesAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
//                                categoriesRecycler.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
