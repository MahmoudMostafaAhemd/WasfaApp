package com.example.mahmouddiab.chatbotex.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.MainActivity;
import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.details.ActivityDetails;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud.diab on 4/7/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter {
    public static final int NORMAL_TYPE = 1;
    public static final int FIRST_TYPE = 2;
    private final int VIEW_PROG = 0;
    private MainMealModel mainMealModel;
    private Context context;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private MyItemDecoration itemDecoration;
    private MainMealModel categoreisModel;


    public RecipesAdapter(Context context, MainMealModel mainMealModel, MainMealModel categoreisModel, RecyclerView recyclerView) {
        this.context = context;
        this.mainMealModel = mainMealModel;
        this.categoreisModel = categoreisModel;
        itemDecoration = new MyItemDecoration(context, R.dimen._2sdp);


        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
//        if (viewType == VIEW_PROG) {
//            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_prog, parent, false);
//            return new ProgressViewHolder(v);
//        } else
        if (viewType == FIRST_TYPE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item_type, parent, false);
            return new CategoriesHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_item, parent, false);
            return new Holder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == FIRST_TYPE) {
            ((CategoriesHolder) holder).recyclerView.setHasFixedSize(true);
            ((CategoriesHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
            ((CategoriesHolder) holder).recyclerView.addItemDecoration(itemDecoration);
            ((CategoriesHolder) holder).recyclerView.setItemViewCacheSize(20);
            ((CategoriesHolder) holder).recyclerView.setDrawingCacheEnabled(true);
            ((CategoriesHolder) holder).recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(context, false, categoreisModel);
            ((CategoriesHolder) holder).recyclerView.setAdapter(categoriesAdapter);
            ((CategoriesHolder) holder).recyclerCard.setVisibility(View.VISIBLE);
        } else if (getItemViewType(position) == NORMAL_TYPE) {

            final String imgUrl = mainMealModel.getImg().get(position)
                    .replace("mobile", "")
                    .replace("-large-card", "web-slider")
                    .replace("-watermarked", "");

            Picasso.with(((Holder) holder).categoryCover.getContext())
                    .load(imgUrl)
                    .into(((Holder) holder).categoryCover);


            ((Holder) holder).mealName.setText(mainMealModel.getName().get(position));
            ((Holder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((MainActivity) context, (((Holder) holder)).categoryCover, "uniLogo");
                    ActivityDetails.start(context, imgUrl, mainMealModel.getUrl().get((((Holder) holder)).getAdapterPosition()),
                            mainMealModel.getName().get(holder.getAdapterPosition()), options.toBundle(), true, false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mainMealModel.getName().size();
    }

    @Override
    public int getItemViewType(int position) {
//        return mainMealModel.getName().get(position) != null ? NORMAL_TYPE : VIEW_PROG;
        if (mainMealModel.getName() == null) {
            return VIEW_PROG;
        } else if (position == 0) {
            return FIRST_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setStartLoading() {
        loading = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView categoryCover;
        @BindView(R.id.meal_name)
        TextView mealName;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CategoriesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categories_recyclerview)
        RecyclerView recyclerView;
        @BindView(R.id.recycler_card)
        CardView recyclerCard;

        public CategoriesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loading)
        ProgressBar loading;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
