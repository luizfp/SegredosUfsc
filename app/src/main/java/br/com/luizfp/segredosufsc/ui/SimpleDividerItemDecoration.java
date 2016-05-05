package br.com.luizfp.segredosufsc.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.util.L;


public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = SimpleDividerItemDecoration.class.getSimpleName();
    private Drawable mDivider;
    private int mMargin;

    public SimpleDividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.partial_line_divider);
        mMargin = context.getResources().getDimensionPixelSize(R.dimen.margin_simple_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            L.d(TAG, "left: " + left);
            L.d(TAG, "margin: " + mMargin);
            L.d(TAG, "right: " + right);
            L.d(TAG, "top: " + top);
            L.d(TAG, "botoom: " + bottom);

            mDivider.setBounds(left + mMargin, top, ((right - mMargin) >= 0) ? right - mMargin : right, bottom);
            mDivider.draw(c);
        }
    }
}