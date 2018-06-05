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
import com.example.mahmouddiab.chatbotex.adapters.AdvicesAdapter;
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
public class AdvicesFragment extends Fragment {

    private MainMealModel mainMealModel;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.refresh)
    TextView refresh;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    private int page = 1;
    private int size;
    private AdvicesAdapter advicesAdapter;

    public AdvicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advices, container, false);
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
        MyItemDecoration itemDecoration = new MyItemDecoration(getContext(), R.dimen._2sdp);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mainMealModel = new MainMealModel();
        advicesAdapter = new AdvicesAdapter(getContext(), mainMealModel, recyclerView, true);
        recyclerView.setAdapter(advicesAdapter);
        advicesAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mainMealModel.addToNames(null);
                advicesAdapter.notifyItemInserted(mainMealModel.getName().size() - 1);
                callPaging();
            }
        });
        callPaging();
    }

    private void callPaging() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.supermama.me/all/بيت?page=" + (page)).get();
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
                                if (page != 1) {
                                    mainMealModel.getName().remove(size);
                                    advicesAdapter.notifyItemRemoved(size);
                                    advicesAdapter.notifyItemRangeChanged(size, size);
                                }
                                page++;
                                loading.setVisibility(View.GONE);
                                advicesAdapter.setLoaded();
                                size = mainMealModel.getName().size();
                                advicesAdapter.notifyItemInserted(mainMealModel.getName().size());
                            }
                        });
                } catch (IOException e) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                                if (page != 1) {
                                    mainMealModel.getName().remove(size);
                                    advicesAdapter.notifyItemRemoved(size);
                                    advicesAdapter.notifyItemRangeChanged(size, size);
                                } else {
                                    loading.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    refreshIcon.setVisibility(View.VISIBLE);
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
        refreshIcon.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        callPaging();
    }

    @OnClick(R.id.refresh_icon)
    void onRefreshIconClicked() {

        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        refreshIcon.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        callPaging();
    }

}
