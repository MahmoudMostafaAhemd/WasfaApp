package com.example.mahmouddiab.chatbotex.mainPager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.mahmouddiab.chatbotex.adapters.CategoriesAdapter;
import com.example.mahmouddiab.chatbotex.adapters.CustomAdapter;
import com.example.mahmouddiab.chatbotex.adapters.RecipesAdapter;
import com.example.mahmouddiab.chatbotex.utils.EqualSpacingItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment {

    final String base = "https://www.ma7shy.com/search?sort=publishDate&direction=desc";
    private MainMealModel mainMealModel;
    private MainMealModel categoriesModel;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.refresh)
    TextView refresh;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcom;
    //    @BindView(R.id.categories_recyclerview)
//    RecyclerView categoriesRecycler;
//    @BindView(R.id.recycler_card)
//    CardView recyclerCard;
    private RecipesAdapter recipesAdapter;
    //    private CategoriesAdapter categoriesAdapter;
    private int page = 1;
    private int size;

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        MyItemDecoration itemDecoration = new MyItemDecoration(getContext(), R.dimen._2sdp);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mainMealModel = new MainMealModel();
        categoriesModel = new MainMealModel();

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (recipesAdapter.getItemViewType(position)) {
                    case RecipesAdapter.FIRST_TYPE:
                        return 2;
                    case RecipesAdapter.NORMAL_TYPE:
                        return 1;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recipesAdapter = new RecipesAdapter(getContext(), mainMealModel, categoriesModel, recyclerView);
        recyclerView.setAdapter(recipesAdapter);

        recipesAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                mainMealModel.addToNames(null);
//                recipesAdapter.notifyItemInserted(mainMealModel.getName().size() - 1);
                callPaging();
            }
        });
//
//
//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
//        categoriesRecycler.setHasFixedSize(true);
//        categoriesRecycler.setLayoutManager(gridLayoutManager);
//        categoriesRecycler.addItemDecoration(itemDecoration);
//        categoriesRecycler.setItemViewCacheSize(20);
//        categoriesRecycler.setDrawingCacheEnabled(true);
//        categoriesRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//
//        categoriesAdapter = new CategoriesAdapter(getContext(), categoriesModel);
//        categoriesRecycler.setAdapter(categoriesAdapter);
        callPaging();
        callHomePage();
    }

    private void callHomePage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.ma7shy.com").get();

                    Elements elements1 = doc.select("div[class=col-xl-4  col-lg-4 img-blo col-md-8  col-sm-8 col-xs-16 ]");
                    for (int i = 0; i < 18; i += 2) {
                        categoriesModel.addToUrl(elements1.get(i).select("a").attr("href"));
                        categoriesModel.addToImg(elements1.get(i).select("img").attr("src"));
                        categoriesModel.addToNames(elements1.get(i).select("img").attr("alt"));
                    }

                    categoriesModel.addToNames(getString(R.string.more));
                    categoriesModel.addToImg(null);


                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                recyclerCard.setVisibility(View.VISIBLE);
//                                page++;
//                                loading.setVisibility(View.GONE);
                                recipesAdapter.notifyDataSetChanged();
                            }
                        });
                } catch (IOException e) {
                    if ((getActivity()) != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                loading.setVisibility(View.GONE);
//                                categoriesRecycler.setVisibility(View.GONE);
                            }
                        });

                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void callPaging() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.supermama.me/all/%D9%88%D8%B5%D9%81%D8%A7%D8%AA?page=" + page).get();
                    Elements elements = doc.select("div[class=cards clearfix cards--grid]").select("div[class=card]");

                    for (int i = 0; i < elements.size(); i++) {
                        mainMealModel.addToUrl(elements.get(i).select("div[class=card__image-wrapper]").select("a").attr("href"));
                        mainMealModel.addToImg(elements.get(i).select("div[class=card__image-wrapper]").select("a").select("picture").select("source[data-srcset]").get(0).attr("data-srcset"));
                        mainMealModel.addToNames(elements.get(i).select("div[class=card__details]").text());

                    }


                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (page != 1) {
//                                    mainMealModel.getName().remove(size);
//                                    recipesAdapter.notifyItemRemoved(size);
//                                    recipesAdapter.notifyItemRangeChanged(size, size);
//                                }
                                page++;
                                loading.setVisibility(View.GONE);
                                recipesAdapter.setLoaded();
                                size = mainMealModel.getName().size();
                                recipesAdapter.notifyItemInserted(mainMealModel.getName().size());
                            }
                        });
                } catch (IOException e) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (page == 1) {
                                    loading.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    refreshIcom.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                }
            }
        }).start();
    }

    @OnClick(R.id.refresh)
    void onRefreshClicked() {
        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        refreshIcom.setVisibility(View.GONE);
        callHomePage();
        callPaging();
    }

    @OnClick(R.id.refresh_icon)
    void onRefreshIconClicked() {
        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        refreshIcom.setVisibility(View.GONE);
        callHomePage();
        callPaging();
    }
}
