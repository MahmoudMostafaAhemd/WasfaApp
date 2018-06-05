package com.example.mahmouddiab.chatbotex.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.mahmouddiab.chatbotex.details.ActivityDetails;
import com.example.mahmouddiab.chatbotex.details.AdvicesDetailsActivity;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud.diab on 4/8/2018.
 */

public class AdvicesAdapter extends RecyclerView.Adapter {

    private static int NORMAL_TYPE = 1;
    private final int VIEW_PROG = 0;
    private MainMealModel mainMealModel;
    private Context context;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isAdvice;

    public AdvicesAdapter(Context context, MainMealModel mainMealModel, RecyclerView recyclerView, boolean isAdvice) {
        this.context = context;
        this.mainMealModel = mainMealModel;
        this.isAdvice = isAdvice;
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
        if (viewType == NORMAL_TYPE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.large_image_item, parent, false);
            return new Holder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_prog, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == NORMAL_TYPE) {
            final String url = mainMealModel.getImg().get(position).replace("mobile", "").replace("-large-card", "web-slider").replace("-watermarked", "");
            Picasso.with(((Holder) holder).categoryCover.getContext()).load(url)
                    .into(((Holder) holder).categoryCover);


            ((Holder) holder).mealName.setText(mainMealModel.getName().get(position));
            if (isAdvice) {
                ((Holder) holder).tag.setText(R.string.advices);
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.cyan_500));

            } else {
                ((Holder) holder).tag.setText(R.string.health);
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, (((Holder) holder)).categoryCover, "uniLogo");
                    AdvicesDetailsActivity.start(context, url, mainMealModel.getUrl().get((((Holder) holder)).getAdapterPosition()), options.toBundle());
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
        return mainMealModel.getName().get(position) != null ? NORMAL_TYPE : VIEW_PROG;
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
        @BindView(R.id.tagText)
        TextView tag;

        public Holder(View itemView) {
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
