package com.example.mahmouddiab.chatbotex.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.MainMealModel;
import com.example.mahmouddiab.chatbotex.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud.diab on 4/5/2018.
 */

public class LargeImageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainMealModel sliderModels;
    private Context context;


    public LargeImageViewAdapter(Context context, MainMealModel sliderModels) {
        this.sliderModels = sliderModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.large_image_item, parent, false);
        Holder vh = new Holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String url = sliderModels.getImg().get(position).replace("widget", "large");
        Picasso.with(((Holder) holder).categoryCover.getContext()).load(url)
                .into(((Holder) holder).categoryCover);

//        ((Holder) holder).mealName.setText(sliderModels.getName().get(position));

    }


    @Override
    public int getItemCount() {
        return sliderModels.getName().size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView categoryCover;
//        @BindView(R.id.meal_name)
//        TextView mealName;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}