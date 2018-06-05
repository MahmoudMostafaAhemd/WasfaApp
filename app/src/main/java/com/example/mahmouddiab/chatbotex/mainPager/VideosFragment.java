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
import com.example.mahmouddiab.chatbotex.VideosPresenterImpl;
import com.example.mahmouddiab.chatbotex.VideosView;
import com.example.mahmouddiab.chatbotex.adapters.AdvicesAdapter;
import com.example.mahmouddiab.chatbotex.adapters.VideosAdapter;
import com.example.mahmouddiab.chatbotex.models.Item;
import com.example.mahmouddiab.chatbotex.models.YoutubeChannelId;
import com.example.mahmouddiab.chatbotex.models.YoutubeVideos;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment implements VideosView {

    private VideosPresenterImpl videosPresenter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.refresh)
    TextView refresh;
    @BindView(R.id.refresh_icon)
    ImageView refreshIcon;
    private VideosAdapter videosAdapter;
    private YoutubeVideos youtubeVideos;
    private String pageToken = "";
    private int page = 1;

    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
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
        youtubeVideos = new YoutubeVideos();
        videosAdapter = new VideosAdapter(getContext(), youtubeVideos, recyclerView);
        recyclerView.setAdapter(videosAdapter);
        videosAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                youtubeVideos.getItems().add(null);
//                videosAdapter.notifyItemInserted(youtubeVideos.getItems().size() - 1);
                page++;
                videosPresenter.getVideos(20, pageToken, "UUrE5w3IopeMfvRgmkFKjBAA", "AIzaSyCFFXBCUe6XnMoNmoAtuTJxo-Decag_4VU");
            }
        });
        videosPresenter = new VideosPresenterImpl(this);
        videosPresenter.getVideos(20, pageToken, "UUrE5w3IopeMfvRgmkFKjBAA", "AIzaSyCFFXBCUe6XnMoNmoAtuTJxo-Decag_4VU");
    }

    @Override
    public void getVideos(YoutubeVideos youtubeVideos) {
        this.youtubeVideos.getItems().addAll(youtubeVideos.getItems());
//        if (page != 1) {
//            youtubeVideos.getItems().remove(size);
//            videosAdapter.notifyItemRemoved(size);
//            videosAdapter.notifyItemRangeChanged(size, size);
//        }

        loading.setVisibility(View.GONE);
        videosAdapter.setLoaded();
        pageToken = youtubeVideos.getNextPageToken();
        videosAdapter.notifyItemInserted(youtubeVideos.getItems().size());
    }

    @Override
    public void getVideosError(String error) {
        if (page == 1) {
            loading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
            refreshIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetChannelId(YoutubeChannelId youtubeChannelId) {

    }

    @OnClick(R.id.refresh)
    void onRefreshClicked() {
        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        refreshIcon.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        videosPresenter.getVideos(20, pageToken, "UUrE5w3IopeMfvRgmkFKjBAA", "AIzaSyCFFXBCUe6XnMoNmoAtuTJxo-Decag_4VU");

    }

    @OnClick(R.id.refresh_icon)
    void onRefreshIconClicked() {
        loading.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        refreshIcon.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        videosPresenter.getVideos(20, pageToken, "UUrE5w3IopeMfvRgmkFKjBAA", "AIzaSyCFFXBCUe6XnMoNmoAtuTJxo-Decag_4VU");
    }
}
