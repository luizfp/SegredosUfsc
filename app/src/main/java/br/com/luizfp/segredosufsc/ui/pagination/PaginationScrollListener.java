package br.com.luizfp.segredosufsc.ui.pagination;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by luiz on 2/24/16.
 */
@SuppressWarnings("FieldCanBeLocal")
public final class PaginationScrollListener extends RecyclerView.OnScrollListener {
    // The minimum number of items remaining before we should loading more.
    private static final int VISIBLE_THRESHOLD = 3;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private final LinearLayoutManager linearLayoutManager;
    private final OnScrollListener callback;

    public interface OnScrollListener {
        void onLoadMore();
    }

    public PaginationScrollListener(OnScrollListener callback, LinearLayoutManager linearLayoutManager) {
        this.callback = callback;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // check for scroll down
        if(dy > 0) {
            visibleItemCount = linearLayoutManager.getChildCount();
            totalItemCount = linearLayoutManager.getItemCount();
            firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
            if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                //Do pagination.. i.e. fetch new data
                if (callback != null) {
                    callback.onLoadMore();
                }
            }
        }
    }
}
