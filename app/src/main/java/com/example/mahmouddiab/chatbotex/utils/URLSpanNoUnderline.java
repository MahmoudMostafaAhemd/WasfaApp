package com.example.mahmouddiab.chatbotex.utils;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by mahmoud.diab on 4/10/2018.
 */

public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }
    @Override public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}