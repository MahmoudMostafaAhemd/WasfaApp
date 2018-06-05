package com.example.mahmouddiab.chatbotex.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mahmouddiab.chatbotex.R;

/**
 * Created by mahmoud.diab on 4/4/2018.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public MyItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public MyItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}