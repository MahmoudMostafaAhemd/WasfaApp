package com.example.mahmouddiab.chatbotex.mainPager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.adapters.CustomAdapter;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    final String base = "https://www.ma7shy.com";
    private ArrayList<MainMealModel> mainMealModel;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.refresh)
    TextView refresh;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    private CustomAdapter customAdapter;
    private int page = 1;
    private int size;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        MyItemDecoration itemDecoration = new MyItemDecoration(getContext(), R.dimen._5sdp);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mainMealModel = new ArrayList<>();
        customAdapter = new CustomAdapter(getContext(), mainMealModel, recyclerView);
        recyclerView.setAdapter(customAdapter);


        customAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (page == 1 || page == 2) {
                    callSecondPage();
                } else {
                    mainMealModel.add(null);
                    customAdapter.notifyItemInserted(mainMealModel.size() - 1);
                    callHome();
                }
            }
        });


        callHomePage();
    }

    private void callHome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.ma7shy.com/search?sort=publishDate&direction=desc&page=" + (page - 2) + "&limit=40").get();
                    Elements elements = doc.select("div[id=listView]").select("div[class=row tiplist-block]");
                    MainMealModel meal;
                    for (int i = 0; i < elements.size(); i++) {
                        meal = new MainMealModel();
                        meal.addToUrl(elements.select("div[class=col-xl-2  col-lg-3 col-xs-6]").get(i).select("a").attr("href"));
                        meal.addToImg(elements.select("div[class=col-xl-2  col-lg-3 col-xs-6]").get(i).select("img").attr("src"));
                        meal.addToNames(elements.select("div[class=col-xl-2  col-lg-3 col-xs-6]").get(i).select("img").attr("alt"));
                        meal.addToTags(elements.get(i).select("a[class=btn btn-danger]").get(0).text());
                        mainMealModel.add(meal);
                    }

                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (page > 2) {
                                    mainMealModel.remove(size);
                                    customAdapter.notifyItemRemoved(size);
                                    customAdapter.notifyItemRangeChanged(size, size);
                                }
                                page++;
                                customAdapter.setLoaded();
                                size = mainMealModel.size();
                                customAdapter.notifyItemInserted(mainMealModel.size());

                            }
                        });
                } catch (IOException e) {
                    if ((getActivity()) != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                            }
                        });

                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void callSecondPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.supermama.me/%D9%88%D8%B5%D9%81%D8%A7%D8%AA").get();
                    Elements elements = doc.select("main[class=recipes-category]").select("div[class=cards clearfix cards--full-width]");
                    MainMealModel meal1 = new MainMealModel();
                    for (int i = 1; i < elements.size(); i++) {
                        for (int x = 0; x < elements.get(i).select("div[class=card]").size(); x++) {
                            meal1.addToUrl(elements.get(i).select("div[class=card]").get(x).select("div[class=card__image-wrapper]").select("a").attr("href"));
                            meal1.addToImg(elements.get(i).select("div[class=card]").get(x).select("div[class=card__image-wrapper]").select("a").select("picture").select("source[data-srcset]").get(0).attr("data-srcset"));
                            meal1.addToNames(elements.get(i).select("div[class=card]").get(x).select("div[class=card__details]").text());
                        }
                    }

                    mainMealModel.add(meal1);


                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                loading.setVisibility(View.GONE);
                                customAdapter.setLoaded();
                                customAdapter.notifyItemInserted(mainMealModel.size());
                                size = mainMealModel.size();
                            }
                        });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //get(1).select("div[class=card]")allItems
//get(1).select("div[class=card]").get(0).select("div[class=card__image-wrapper]").select("a").attr("href")itemLink
    //get(1).select("div[class=card]").get(0).select("div[class=card__image-wrapper]").select("a").select("picture").select("source[data-srcset]").get(0).attr("data-srcset")image
    //get(1).select("div[class=card]").get(0).select("div[class=card__details]").text() title
    private void callHomePage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(base).get();
                    Elements elements = doc.select("div[class=col-xl-16]")
                            .select("div[class=col-xl-4 col-lg-4  col-md-8 col-sm-8 col-xs-16 img-blo widget_padding]");
                    MainMealModel meal = new MainMealModel();
                    for (int i = 0; i < elements.size(); i++) {
                        meal.addToUrl(elements.get(i).select("a").attr("href"));
                        meal.addToImg(elements.get(i).select("img").attr("src"));
                        meal.addToNames(elements.get(i).select("img").attr("alt"));
                    }

                    mainMealModel.add(meal);
                    MainMealModel meal1 = new MainMealModel();
                    Elements elements1 = doc.select("div[class=col-xl-4  col-lg-4 img-blo col-md-8  col-sm-8 col-xs-16 ]");
                    for (int i = 0; i < 8; i++) {
                        meal1.addToUrl(elements1.get(i).select("a").attr("href"));
                        meal1.addToImg(elements1.get(i).select("img").attr("src"));
                        meal1.addToNames(elements1.get(i).select("img").attr("alt"));
                    }
                    mainMealModel.add(meal1);
                    MainMealModel kitchen = new MainMealModel();
                    for (int i = 13; i < elements1.size(); i++) {
                        kitchen.addToUrl(elements1.get(i).select("a").attr("href"));
                        kitchen.addToImg(elements1.get(i).select("img").attr("src"));
                        kitchen.addToNames(elements1.get(i).select("img").attr("alt"));
                    }

                    mainMealModel.add(kitchen);

                    Random r = new Random();
                    int Low = 7;
                    int High = 11;
                    int random = r.nextInt(High - Low) + Low;
                    MainMealModel largeImage = new MainMealModel();
                    largeImage.addToUrl(elements1.get(random).select("a").attr("href"));
                    largeImage.addToImg(elements1.get(random).select("img").attr("src"));
                    largeImage.addToNames(elements1.get(random).select("img").attr("alt"));

                    mainMealModel.add(largeImage);

                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                loading.setVisibility(View.GONE);
                                customAdapter.setLoaded();
                                customAdapter.notifyItemInserted(mainMealModel.size());
                            }
                        });
                } catch (IOException e) {
                    if ((getActivity()) != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                                if (page > 2) {
                                    mainMealModel.remove(size);
                                    customAdapter.notifyItemRemoved(size);
                                    customAdapter.notifyItemRangeChanged(size, size);
                                } else {
                                    loading.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    refreshIcon.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @OnClick(R.id.refresh)
    void onRefreshClicked() {
        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        refreshIcon.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        callHomePage();
    }

    @OnClick(R.id.refresh_icon)
    void onRefreshIconClicked() {
        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        refreshIcon.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        callHomePage();
    }

}
