package com.example.mahmouddiab.chatbotex.details;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.adapters.KitchenRecipesAdapter;
import com.example.mahmouddiab.chatbotex.base.BaseActivity;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.no_result)
    TextView noResult;
    private KitchenRecipesAdapter kitchenRecipesAdapter;
    private MainMealModel mainMealModel;
    private int page = 1;
    private int size;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_avtivity);
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
        name = getIntent().getStringExtra("name");
        title.setText(name);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mainMealModel = new MainMealModel();
        kitchenRecipesAdapter = new KitchenRecipesAdapter(this, mainMealModel, recyclerView);
        recyclerView.setAdapter(kitchenRecipesAdapter);
        kitchenRecipesAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mainMealModel.addToNames(null);
                kitchenRecipesAdapter.notifyItemInserted(mainMealModel.getName().size() - 1);
                callHome();
            }
        });
        callHome();
    }

    public static void start(Context context, String name) {
        Intent starter = new Intent(context, SearchActivity.class);
        starter.putExtra("name", name);
        context.startActivity(starter);
    }

    private void callHome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.ma7shy.com/search?q=" + name + "&page=" + page + "&limit=40").get();
                    Elements elements = doc.select("div[id=listView]").select("div[class=row tiplist-block]");

                    for (int i = 0; i < elements.size(); i++) {

                        mainMealModel.addToUrl(elements.select("div[class=col-xl-2  col-lg-3 col-xs-6]").get(i).select("a").attr("href"));
                        mainMealModel.addToImg(elements.select("div[class=col-xl-2  col-lg-3 col-xs-6]").get(i).select("img").attr("src"));
                        mainMealModel.addToNames(elements.select("div[class=col-xl-2  col-lg-3 col-xs-6]").get(i).select("img").attr("alt"));
                        mainMealModel.addToTags(elements.get(i).select("a[class=btn btn-danger]").get(0).text());

                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (page != 1) {
                                mainMealModel.getName().remove(size);
                                kitchenRecipesAdapter.notifyItemRemoved(size);
                                kitchenRecipesAdapter.notifyItemRangeChanged(size, size);
                            } else {
                                if (mainMealModel.getName().size() == 0)
                                    noResult.setVisibility(View.VISIBLE);
                            }
                            size = mainMealModel.getName().size();
                            page++;
                            loading.setVisibility(View.GONE);
                            kitchenRecipesAdapter.setLoaded();

                            kitchenRecipesAdapter.notifyItemInserted(mainMealModel.getName().size());

                        }
                    });
                } catch (IOException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

            }

        }).start();
    }
}
