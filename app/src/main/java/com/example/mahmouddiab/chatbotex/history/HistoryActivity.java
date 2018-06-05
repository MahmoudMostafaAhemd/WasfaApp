package com.example.mahmouddiab.chatbotex.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.DataBase;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.adapters.HistoryAdapter;
import com.example.mahmouddiab.chatbotex.models.RecipeModel;
import com.example.mahmouddiab.chatbotex.utils.MyItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //    @BindView(R.id.loading)
//    ProgressBar loading;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<RecipeModel> recipeModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        MyItemDecoration itemDecoration = new MyItemDecoration(this, R.dimen._5sdp);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recipeModels = new ArrayList<>();
        for (int i = 0; i < DataBase.getInstance().getAllItems().size(); i++) {
            recipeModels.add(DataBase.getInstance()
                    .getItemFromHistory(DataBase.getInstance().getAllItems().get(i)));
        }
        Collections.reverse(recipeModels);
        HistoryAdapter historyAdapter = new HistoryAdapter(this, recipeModels, recyclerView);
        recyclerView.setAdapter(historyAdapter);

    }
}
