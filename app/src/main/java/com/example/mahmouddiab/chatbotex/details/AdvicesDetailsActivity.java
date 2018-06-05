package com.example.mahmouddiab.chatbotex.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.base.BaseActivity;
import com.example.mahmouddiab.chatbotex.utils.URLSpanNoUnderline;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvicesDetailsActivity extends BaseActivity {

    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.recipeDetails)
    TextView recipeDetails;
    @BindView(R.id.recipeName)
    TextView recipeName;
    @BindView(R.id.recipe_image)
    ImageView recipeImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    View container;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.source)
    TextView source;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advices_details);
        ButterKnife.bind(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        MobileAds.initialize(this,
                "ca-app-pub-1465589563932518~8447946529");
        mInterstitialAd.setAdUnitId("ca-app-pub-1465589563932518/1757083502");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        progressBar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final String imageUrl = getIntent().getStringExtra("imageUrl");
        final String url = getIntent().getStringExtra("url");
        Picasso.with(this).load(imageUrl)
                .into(recipeImage);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float offsetAlpha = (appBarLayout.getY() / appBarLayout.getTotalScrollRange());
                Log.v("offset", offsetAlpha + "");
                title.setAlpha(-1 * (offsetAlpha));

            }
        });
        callAdviceDetailsFromSuperMama(url);
    }

    public static void start(Context context, String imageUrl, String url, Bundle options) {
        Intent starter = new Intent(context, AdvicesDetailsActivity.class);
        starter.putExtra("imageUrl", imageUrl);
        starter.putExtra("url", url);
        context.startActivity(starter, options);
    }

    private void callAdviceDetailsFromSuperMama(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Document doc = Jsoup.connect(url).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            container.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            recipeName.setVisibility(View.VISIBLE);
                            recipeDetails.setVisibility(View.VISIBLE);
                            recipeName.setText(doc.select("h1[class=article__header__title]").text());
                            for (int i = 0; i < doc.select("div[class=article__content]").select("p").size(); i++) {
                                if (TextUtils.isEmpty(doc.select("div[class=article__content]").select("p").get(i).attr("dir"))) {
                                    doc.select("div[class=article__content]").select("p").get(i).select("a[href]").remove();
                                }
                            }

//                            doc.select("div[class=article__content]").select("dl").remove();
//                            doc.select("div[class=article__content]").select("h4").remove();
                            String html = doc.select("div[class=article__content]").html();
                            recipeDetails.setMovementMethod(LinkMovementMethod.getInstance());
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                recipeDetails.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
                                source.setText(Html.fromHtml(getString(R.string.supermama), Html.FROM_HTML_MODE_LEGACY));
                            }else {
                                recipeDetails.setText(Html.fromHtml(html));
                                source.setText(Html.fromHtml(getString(R.string.supermama)));
                            }

//                            stripUnderlines(recipeDetails);

                            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent)); // transperent color = #00000000
                            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                            collapsingToolbarLayout.setEnabled(false);
                            title.setText(doc.select("h1[class=article__header__title]").text());
                        }
                    });
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            super.onBackPressed();
        }else super.onBackPressed();
    }
}
