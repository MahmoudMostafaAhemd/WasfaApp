package com.example.mahmouddiab.chatbotex.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
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
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.example.mahmouddiab.chatbotex.utils.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter {
    private static int NORMAL_TYPE = 1;
    private static int SLIDER_TYPE = 2;
    private static int SLIDER_TYPE_FEATURED = 3;
    private static int SLIDER_TYPE_GRID = 4;
    private static int LARGE_IMAGE = 5;
    private static int STREET_FOOD = 6;
    private final int VIEW_PROG = 0;
    private ArrayList<MainMealModel> mainMealModel;
    private Context context;
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private MyItemDecoration itemDecoration;

    public CustomAdapter(Context context, ArrayList<MainMealModel> mainMealModel, RecyclerView recyclerView) {
        this.context = context;
        this.mainMealModel = mainMealModel;
        itemDecoration = new MyItemDecoration(context, R.dimen._4sdp);
//        recycledViewPool = new RecyclerView.RecycledViewPool();
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

//    public void addItems(TrendingCoursesModel mainMealModel) {
//        this.mainMealModel.addAllCourseNameItem(mainMealModel.getCourseName());
//        this.mainMealModel.addAllCourseUrlItem(mainMealModel.getCourseUrl());
//        this.mainMealModel.addAllDates(mainMealModel.getDates());
//        this.mainMealModel.addAllProviderNameItem(mainMealModel.getProviderName());
//        this.mainMealModel.addALLRate(mainMealModel.getCourseRate());
//        this.mainMealModel.addAllUniNameItem(mainMealModel.getUniversityName());
//        this.mainMealModel.addAllUniversityUrlItem(mainMealModel.getUniversityUrl());
//        notifyDataSetChanged();
//    }


//    public void clear() {
//        mainMealModel.getImg().clear();
//        mainMealModel.getName().clear();
//        mainMealModel.getUrl().clear();
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemViewType(int position) {
//        return mainMealModel.getName().get(position) != null ? NORMAL_TYPE : VIEW_PROG;
        if (mainMealModel.get(position) == null)
            return VIEW_PROG;
        else if (position == 0)
            return SLIDER_TYPE;
        else if (position == 1) {
            return SLIDER_TYPE_FEATURED;
        } else if (position == 2 || position == 4) {
            return SLIDER_TYPE_GRID;
        } else if (position == 3) {
            return LARGE_IMAGE;
        } else if (position == mainMealModel.size() - 1)
            return STREET_FOOD;
        else return LARGE_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_PROG) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_prog, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType == NORMAL_TYPE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_meals_hori_view, parent, false);
            return new MyViewHolder(v);
        } else if (viewType == LARGE_IMAGE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.large_image_item, parent, false);
            return new LargeImageHolder(v);
        } else if (viewType == STREET_FOOD) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.street_food_image, parent, false);
            return new LargeImageHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_meals_hori_view, parent, false);
            return new Holder(v);
        }
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == NORMAL_TYPE) {
//            if (!TextUtils.isEmpty(mainMealModel.getUniversityName().get(position)))
//                ((MyViewHolder) holder).uniName.setText(mainMealModel.getUniversityName().get(position));
//            else ((MyViewHolder) holder).uniName.setText(context.getString(R.string.not_available));
//            ((MyViewHolder) holder).courseName.setText(mainMealModel.getCourseName().get(position));
//            ((MyViewHolder) holder).providerName.setText(mainMealModel.getProviderName().get(position).split("[0-9]+")[0]);
//            for (int i = 0; i < mainMealModel.getProviderName().get(position).length(); i++) {
//                if (Character.isDigit(mainMealModel.getProviderName().get(position).charAt(i))) {
//                    ((MyViewHolder) holder).courseHours.setText(mainMealModel.getProviderName().get(position).substring(i));
//                    break;
//                }
//            }
//            if (TextUtils.isEmpty(((MyViewHolder) holder).courseHours.getText().toString())) {
//                ((MyViewHolder) holder).courseHours.setText(context.getString(R.string.not_available));
//            }
//            if (!TextUtils.isEmpty(mainMealModel.getCourseRate().get(position))
//                    && Character.isDigit(mainMealModel.getCourseRate().get(position).charAt(0))) {
//                ((MyViewHolder) holder).materialRatingBar.setRating(Float.parseFloat(mainMealModel.getCourseRate().get(position)));
//                ((MyViewHolder) holder).rates.setText(mainMealModel.getCourseRate().get(position));
//            } else {
//                ((MyViewHolder) holder).materialRatingBar.setRating(0f);
//                ((MyViewHolder) holder).rates.setText(context.getString(R.string.not_available));
//            }
//            ((MyViewHolder) holder).rates.setText(mainMealModel.getCourseRate().get(position));
//            if (!TextUtils.isEmpty(mainMealModel.getDates().get(position)))
//                ((MyViewHolder) holder).date.setText(mainMealModel.getDates().get(position));
//            else ((MyViewHolder) holder).date.setText(context.getString(R.string.not_available));
//
//            ((MyViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CourseDetailsActivity.start(context, mainMealModel.getCourseUrl().get(holder.getAdapterPosition()));
//                }
//            });
        } else if (getItemViewType(position) == SLIDER_TYPE && !mainMealModel.isEmpty()) {
            HorizontalAdapter horizontalAdapter = new HorizontalAdapter(context, mainMealModel.get(position));
            ((Holder) holder).todayMeal.setText(context.getString(R.string.today_meal));
            ((Holder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


            ((Holder) holder).recyclerView.removeItemDecoration(itemDecoration);
            ((Holder) holder).recyclerView.addItemDecoration(itemDecoration);
            SnapHelper snapHelperStart = new PagerSnapHelper();
            ((Holder) holder).recyclerView.setOnFlingListener(null);
            snapHelperStart.attachToRecyclerView(((Holder) holder).recyclerView);
//            ((Holder) holder).recyclerView.setOnFlingListener(null);
            ((Holder) holder).recyclerView.setAdapter(horizontalAdapter);


            ((Holder) holder).recyclerView.setHasFixedSize(true);
            ((Holder) holder).recyclerView.setItemViewCacheSize(20);
            ((Holder) holder).recyclerView.setDrawingCacheEnabled(true);
            ((Holder) holder).recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        } else if (getItemViewType(position) == SLIDER_TYPE_FEATURED && !mainMealModel.isEmpty()) {
            HorizontalSecAdapter horizontalAdapter = new HorizontalSecAdapter(context, mainMealModel.get(position));
            ((Holder) holder).todayMeal.setText(context.getString(R.string.featured_category));
            ((Holder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelperStart = new PagerSnapHelper();

            ((Holder) holder).recyclerView.removeItemDecoration(itemDecoration);
            ((Holder) holder).recyclerView.addItemDecoration(itemDecoration);
            ((Holder) holder).recyclerView.setOnFlingListener(null);
            snapHelperStart.attachToRecyclerView(((Holder) holder).recyclerView);
//            ((Holder) holder).recyclerView.setOnFlingListener(null);
            ((Holder) holder).recyclerView.setAdapter(horizontalAdapter);


            ((Holder) holder).recyclerView.setHasFixedSize(true);
            ((Holder) holder).recyclerView.setItemViewCacheSize(20);
            ((Holder) holder).recyclerView.setDrawingCacheEnabled(true);
            ((Holder) holder).recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        } else if (getItemViewType(position) == SLIDER_TYPE_GRID && !mainMealModel.isEmpty()) {
            GridHoriAdapter horizontalAdapter = null;
            if (position == 2) {
                ((Holder) holder).todayMeal.setText(context.getString(R.string.kitchens));
                horizontalAdapter = new GridHoriAdapter(context, mainMealModel.get(position), true);
            } else if (position == 4) {
                ((Holder) holder).todayMeal.setText(context.getString(R.string.mix));
                horizontalAdapter = new GridHoriAdapter(context, mainMealModel.get(position), false);
            }
            ((Holder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));

            ((Holder) holder).recyclerView.removeItemDecoration(itemDecoration);
            ((Holder) holder).recyclerView.addItemDecoration(itemDecoration);
            SnapHelper snapHelperStart = new PagerSnapHelper();
            ((Holder) holder).recyclerView.setOnFlingListener(null);
            snapHelperStart.attachToRecyclerView(((Holder) holder).recyclerView);
            ((Holder) holder).recyclerView.setAdapter(horizontalAdapter);


            ((Holder) holder).recyclerView.setHasFixedSize(true);
            ((Holder) holder).recyclerView.setItemViewCacheSize(20);
            ((Holder) holder).recyclerView.setDrawingCacheEnabled(true);
            ((Holder) holder).recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        } else if (getItemViewType(position) == LARGE_IMAGE && !mainMealModel.isEmpty()) {
            final String url;
            if (position == 3) {
                url = mainMealModel.get(position).getImg().get(0).replace("image_recipe_widget", "image_category_recipe_featured");
                ((LargeImageHolder) holder).tag.setVisibility(View.GONE);
                ((LargeImageHolder) holder).mealName.setText(String.format("# %s", mainMealModel.get(position).getName().get(0)));
                ((LargeImageHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((MainActivity) context, ((LargeImageHolder) holder).categoryCover, "uniLogo");
                        KitchenRecipesActivity.start(context, url, mainMealModel.get(holder.getAdapterPosition()).getUrl().get(0),
                                mainMealModel.get(holder.getAdapterPosition()).getName().get(0), options.toBundle(), false);
                    }
                });
            } else {
                url = mainMealModel.get(position).getImg().get(0).replace("image_recipe_widget", "image_recipe_large");
                if (!mainMealModel.get(position).getTags().isEmpty())
                    ((LargeImageHolder) holder).tag.setText(mainMealModel.get(position).getTags().get(0));
                ((LargeImageHolder) holder).mealName.setText(mainMealModel.get(position).getName().get(0));
                if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.seafood))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_500));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.american_food))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.indigo_500));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.oriental))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.oriental));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.asian_food))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.asian));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.turkish_food))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.indian_food))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.indian));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.european_food))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.european));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.meat))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.sweets))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.sweets));
                } else if (mainMealModel.get(position).getTags().get(0).equalsIgnoreCase(context.getString(R.string.drinks))) {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.cyan_500));
                } else {
                    ((LargeImageHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                }

                ((LargeImageHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((MainActivity) context, ((LargeImageHolder) holder).categoryCover, "uniLogo");
                        ActivityDetails.start(context, url, mainMealModel.get(holder.getAdapterPosition()).getUrl().get(0)
                                ,mainMealModel.get(holder.getAdapterPosition()).getName().get(0), options.toBundle(), false,false);
                    }
                });
            }
            Picasso.with(context).load(url)
                    .into(((LargeImageHolder) holder).categoryCover);


        } else if (getItemViewType(position) == STREET_FOOD && !mainMealModel.isEmpty()) {
//

            AnimationDrawable animationDrawable = (AnimationDrawable) ((LargeImageHolder) holder).categoryCover.getBackground();
            animationDrawable.setEnterFadeDuration(5000);
            animationDrawable.setExitFadeDuration(2000);
// onResume
            animationDrawable.start();
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
    public int getItemCount() {
        return mainMealModel.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loading)
        ProgressBar loading;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.uniName)
//        TextView uniName;
//        @BindView(R.id.courseName)
//        TextView courseName;
//        @BindView(R.id.providerName)
//        TextView providerName;
//        @BindView(R.id.stars)
//        MaterialRatingBar materialRatingBar;
//        @BindView(R.id.rates)
//        TextView rates;
//        @BindView(R.id.date)
//        TextView date;
//        @BindView(R.id.cardview)
//        CardView cardView;
//        @BindView(R.id.courseHours)
//        TextView courseHours;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;
        @BindView(R.id.today_meal)
        TextView todayMeal;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LargeImageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView categoryCover;
        @BindView(R.id.meal_name)
        TextView mealName;
        @BindView(R.id.tagText)
        TextView tag;

        public LargeImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}