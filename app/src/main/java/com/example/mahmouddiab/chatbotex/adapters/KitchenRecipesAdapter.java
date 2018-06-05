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

import com.example.mahmouddiab.chatbotex.MainActivity;
import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.details.ActivityDetails;
import com.example.mahmouddiab.chatbotex.details.KitchenRecipesActivity;
import com.example.mahmouddiab.chatbotex.details.SearchActivity;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud.diab on 4/14/2018.
 */

public class KitchenRecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainMealModel mainMealModel;
    private Context context;
    private final int VIEW_PROG = 0;
    private static int LARGE_IMAGE = 5;
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public KitchenRecipesAdapter(Context context, MainMealModel sliderModels, RecyclerView recyclerView) {
        this.mainMealModel = sliderModels;
        this.context = context;
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
        if (viewType == VIEW_PROG) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_prog, parent, false);
            return new ProgressViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kitchen_recipe_item, parent, false);

            return new Holder(v);
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


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == LARGE_IMAGE && !mainMealModel.getName().isEmpty()) {
            final String url;

            url = mainMealModel.getImg().get(position).replace("image_recipe_widget", "image_recipe_large");
            if (!mainMealModel.getTags().isEmpty())
                ((Holder) holder).tag.setText(mainMealModel.getTags().get(position));
            ((Holder) holder).mealName.setText(mainMealModel.getName().get(position));
            if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.seafood))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_500));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.american_food))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.indigo_500));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.oriental))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.oriental));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.asian_food))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.asian));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.turkish_food))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.indian_food))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.indian));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.european_food))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.european));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.meat))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.sweets))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.sweets));
            } else if (mainMealModel.getTags().get(position).equalsIgnoreCase(context.getString(R.string.drinks))) {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.cyan_500));
            } else {
                ((Holder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            }

            Picasso.with(context).load(url)
                    .into(((Holder) holder).categoryCover);
            ((Holder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, ((Holder) holder).categoryCover, "uniLogo");
                    ActivityDetails.start(context, url, mainMealModel.getUrl().get(holder.getAdapterPosition()),
                            mainMealModel.getName().get(holder.getAdapterPosition()),options.toBundle(), false, false);
                }
            });


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mainMealModel.getName().get(position) == null)
            return VIEW_PROG;
        else return LARGE_IMAGE;
    }

    @Override
    public int getItemCount() {
        return mainMealModel.getName().size();
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
