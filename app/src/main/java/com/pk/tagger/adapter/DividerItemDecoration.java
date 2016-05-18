package com.pk.tagger.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.pk.tagger.R;

/**
 * Created by pk on 17/05/16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight = 400;

    private Drawable mDivider;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int childCount = parent.getChildCount();
        if (childCount==5) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }


    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();

        for (int i = 4; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin + mVerticalSpaceHeight;
            int bottom = top + mDivider.getIntrinsicHeight();
            Log.d("child", String.valueOf(top) + "/" + String.valueOf(bottom));

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
