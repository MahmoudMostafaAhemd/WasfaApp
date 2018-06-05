package com.example.mahmouddiab.chatbotex.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.MainActivity;
import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.details.CategoriesActivity;
import com.example.mahmouddiab.chatbotex.details.KitchenRecipesActivity;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud.diab on 4/8/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter {
    private Context context;
    private MainMealModel mainMealModel;
    private MyItemDecoration itemDecoration;
    private boolean isAllItems;

    public CategoriesAdapter(Context context, boolean isAllItems, MainMealModel mainMealModel) {
        this.context = context;
        this.mainMealModel = mainMealModel;
        this.isAllItems = isAllItems;
        itemDecoration = new MyItemDecoration(context, R.dimen._4sdp);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (!TextUtils.isEmpty(mainMealModel.getImg().get(position)))
            Picasso.with(context).load(mainMealModel.getImg().get(position))
                    .into(((MyViewHolder) holder).categoryCover);
        if (position == mainMealModel.getName().size() - 1 && !isAllItems) {
            ((MyViewHolder) holder).mealName.setTextColor(ContextCompat.getColor(context, R.color.mid_gray));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(new Intent(context, CategoriesActivity.class));
                }
            });
        } else {
            ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, ((MyViewHolder) holder).categoryCover, "uniLogo");
                    KitchenRecipesActivity.start(context, mainMealModel.getImg().get(holder.getAdapterPosition())
                            , mainMealModel.getUrl().get((holder).getAdapterPosition()),
                            mainMealModel.getName().get(holder.getAdapterPosition()), options.toBundle(), true);
                }
            });
        }
        ((MyViewHolder) holder).mealName.setText(mainMealModel.getName().get(position));

    }

    @Override
    public int getItemCount() {
        return mainMealModel.getName().size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.categories_image)
        ImageView categoryCover;
        @BindView(R.id.categories_title)
        TextView mealName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
