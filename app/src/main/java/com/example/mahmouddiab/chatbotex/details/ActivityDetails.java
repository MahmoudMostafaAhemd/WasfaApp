package com.example.mahmouddiab.chatbotex.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mahmouddiab.chatbotex.DataBase;
import com.example.mahmouddiab.chatbotex.R;
import com.example.mahmouddiab.chatbotex.base.BaseActivity;
import com.example.mahmouddiab.chatbotex.models.RecipeModel;
import com.example.mahmouddiab.chatbotex.utils.MyTagHandler;
import com.example.mahmouddiab.chatbotex.utils.URLSpanNoUnderline;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityDetails extends BaseActivity {

    @BindView(R.id.image_background)
    ImageView imageBackground;
    @BindView(R.id.recipeTitle)
    TextView recipeTitle;
    @BindView(R.id.recipe)
    HtmlTextView recipe;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;
    //    @BindView(R.id.adView_center)
//    AdView adViewCenter;
    @BindView(R.id.recipe_)
    TextView recipe_;
    @BindView(R.id.recipe_content_title)
    TextView recipeContentTitle;
    @BindView(R.id.recipe_content)
    HtmlTextView recipeContent;
    @BindView(R.id.source)
    TextView source;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    private RecipeModel recipeModel;
    private String imageUrl;
    private boolean isSuperMama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        mInterstitialAd = new InterstitialAd(this);
        MobileAds.initialize(this,
                "ca-app-pub-1465589563932518~8447946529");
        mInterstitialAd.setAdUnitId("ca-app-pub-1465589563932518/1757083502");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
//        AdRequest adRequestCenter = new AdRequest.Builder().build();
//        adViewCenter.loadAd(adRequestCenter);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark)
                , PorterDuff.Mode.MULTIPLY);
        recipeModel = new RecipeModel();
        imageUrl = getIntent().getStringExtra("imageUrl");
        final String url = getIntent().getStringExtra("url");
        isSuperMama = getIntent().getBooleanExtra("isSuperMama", false);
        final boolean isAdvice = getIntent().getBooleanExtra("isAdvice", false);
        String title = getIntent().getStringExtra("title");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        source.setMovementMethod(LinkMovementMethod.getInstance());
        Picasso.with(this).load(imageUrl)
                .into(imageBackground);
        if (!TextUtils.isEmpty(title)) {
            if (DataBase.getInstance().getAllItems().contains(title)) {
                RecipeModel recipeModel = DataBase.getInstance().getItemFromHistory(title);
                recipeTitle.setText(recipeModel.getRecipeTitle());
                recipe.setMovementMethod(LinkMovementMethod.getInstance());
                recipeContent.setMovementMethod(LinkMovementMethod.getInstance());
//                recipe.setText(recipeModel.getRecipe());
//                recipeContent.setText(recipeModel.getRecipeContent());
                progressBar.setVisibility(View.GONE);
                if (!recipeModel.isSuperMama()) {
                    recipe_.setVisibility(View.GONE);
                    recipeContentTitle.setVisibility(View.VISIBLE);
                } else {
                    recipe_.setVisibility(View.VISIBLE);
                    recipeContentTitle.setVisibility(View.VISIBLE);
                }

//                recipe.setHtml(recipeModel.getRecipe());
//                recipeContent.setHtml(recipeModel.getRecipeContent());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    recipe.setText(Html.fromHtml(recipeModel.getRecipe(), Html.FROM_HTML_MODE_LEGACY, null, new MyTagHandler()));
                    recipeContent.setText(Html.fromHtml(recipeModel.getRecipeContent(), Html.FROM_HTML_MODE_LEGACY, null, new MyTagHandler()));
                    if (!recipeModel.isSuperMama()) {
                        source.setText(Html.fromHtml(getString(R.string.ma7shy), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        source.setText(Html.fromHtml(getString(R.string.supermama), Html.FROM_HTML_MODE_LEGACY));
                    }
                } else {
                    recipe.setText(Html.fromHtml(recipeModel.getRecipe(), null, new MyTagHandler()));
                    recipeContent.setText(Html.fromHtml(recipeModel.getRecipeContent(), null, new MyTagHandler()));
                    if (!recipeModel.isSuperMama()) {
                        source.setText(Html.fromHtml(getString(R.string.ma7shy)));
                    } else {
                        source.setText(Html.fromHtml(getString(R.string.supermama)));
                    }
                }
                return;
            }
        }
        if (isAdvice) {
            callAdviceDetailsFromSuperMama(url);
            return;
        }
        if (!isSuperMama)
            callRecipeDetails(url);
        else
            callRecipeDetailsFromSuperMama(url);
    }

    public static void start(Context context, String imageUrl, String url, String title, Bundle options, boolean isSuperMama, boolean isAdvice) {
        Intent starter = new Intent(context, ActivityDetails.class);
        starter.putExtra("imageUrl", imageUrl);
        starter.putExtra("url", url);
        starter.putExtra("isSuperMama", isSuperMama);
        starter.putExtra("isAdvice", isAdvice);
        starter.putExtra("title", title);
        context.startActivity(starter, options);
    }


    public static void start(Context context, String imageUrl, String url, boolean isSuperMama, boolean isAdvice) {
        Intent starter = new Intent(context, ActivityDetails.class);
        starter.putExtra("imageUrl", imageUrl);
        starter.putExtra("url", url);
        starter.putExtra("isSuperMama", isSuperMama);
        starter.putExtra("isAdvice", isAdvice);
        context.startActivity(starter);
    }

    private void callRecipeDetails(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Document doc = Jsoup.connect("https://www.ma7shy.com" + url).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recipeTitle.setText(doc.select("span[itemprop=name]").text());
                            String html = doc.select("div[id=ingredientsContainer]").html();
                            recipe.setMovementMethod(LinkMovementMethod.getInstance());
                            recipeContent.setMovementMethod(LinkMovementMethod.getInstance());
//                            recipe.setHtml(html);
//                            recipeContent.setHtml(doc.select("ol[id=recipeSteps]").html());
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                recipe.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, null, new MyTagHandler()));
                                recipeContent.setText(Html.fromHtml(doc.select("ol[id=recipeSteps]").html()
                                        , Html.FROM_HTML_MODE_LEGACY, null, new MyTagHandler()));
                                source.setText(Html.fromHtml(getString(R.string.ma7shy), Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                recipe.setText(Html.fromHtml(html, null, new MyTagHandler()));
                                recipeContent.setText(Html.fromHtml(doc.select("ol[id=recipeSteps]").html(), null, new MyTagHandler()));
                                source.setText(Html.fromHtml(getString(R.string.ma7shy)));
                            }
                            recipe_.setVisibility(View.GONE);
                            recipeContentTitle.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            recipeModel.setImgUrl(imageUrl);
                            recipeModel.setRecipe(html);
                            recipeModel.setRecipeContent(doc.select("ol[id=recipeSteps]").html());
                            recipeModel.setSuperMama(false);
                            recipeModel.setRecipeTitle(doc.select("span[itemprop=name]").text());
                            DataBase.getInstance().addToHistory(doc.select("span[itemprop=name]").text(), recipeModel);

                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void callRecipeDetailsFromSuperMama(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Document doc = Jsoup.connect(url).get();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recipeTitle.setText(doc.select("h1[class=recipe__title]").text());
                            String html = doc.select("ul[dir=rtl]").html();
                            recipe.setMovementMethod(LinkMovementMethod.getInstance());
                            recipeContent.setMovementMethod(LinkMovementMethod.getInstance());

//                            recipe.setHtml(html);
//                            recipeContent.setHtml(doc.select("ol[dir=rtl]").html(),
//                                    new HtmlResImageGetter(recipeContent));
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                recipe.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, null, new MyTagHandler()));
                                recipeContent.setText(Html.fromHtml(doc.select("ol[dir=rtl]").html(),
                                        Html.FROM_HTML_MODE_LEGACY, null, new MyTagHandler()));
                                source.setText(Html.fromHtml(getString(R.string.supermama), Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                recipe.setText(Html.fromHtml(html, null, new MyTagHandler()));
                                recipeContent.setText(Html.fromHtml(doc.select("ol[dir=rtl]").html(), null, new MyTagHandler()));
                                source.setText(Html.fromHtml(getString(R.string.supermama)));
                            }

                            recipe_.setVisibility(View.VISIBLE);
                            recipeContentTitle.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);


                            recipeModel.setImgUrl(imageUrl);
                            recipeModel.setRecipe(html);
                            recipeModel.setRecipeContent(doc.select("ol[dir=rtl]").html());
                            recipeModel.setSuperMama(true);
                            recipeModel.setRecipeTitle(doc.select("h1[class=recipe__title]").text());
                            DataBase.getInstance().addToHistory(doc.select("h1[class=recipe__title]").text(), recipeModel);
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
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
                            recipeTitle.setText(doc.select("h1[class=article__header__title]").text());
//                            doc.select("div[class=article__content]").select("p").select("a[href]").remove();
//                            doc.select("div[class=article__content]").select("dl").remove();
//                            doc.select("div[class=article__content]").select("h4").remove();
                            String html = doc.select("div[class=article__content]").html();
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                                recipe.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
                            else
                                recipe.setText(Html.fromHtml(html));
                            stripUnderlines(recipe);
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
        } else super.onBackPressed();
    }

//    @OnClick(R.id.add_to_calendar)
//    void onAddToCalendarClicked() {
//        UmmalquraCalendar now = new UmmalquraCalendar();
//        HijriDatePickerDialog dpd = HijriDatePickerDialog.newInstance(
//                this,
//                now.get(UmmalquraCalendar.YEAR),
//                now.get(UmmalquraCalendar.MONTH),
//                now.get(UmmalquraCalendar.DAY_OF_MONTH));
//        dpd.show(getFragmentManager(), "HijriDatePickerDialog");
//    }
//
//    @Override
//    public void onDateSet(HijriDatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//
//    }
}
