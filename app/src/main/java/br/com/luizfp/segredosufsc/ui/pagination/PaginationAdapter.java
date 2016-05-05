package br.com.luizfp.segredosufsc.ui.pagination;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.util.L;

/**
 * Created by luiz on 2/24/16.
 */
public abstract class PaginationAdapter<O> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_PROGRESS = 1;
    private static final String TAG = PaginationAdapter.class.getSimpleName();
    protected final Context context;
    protected final List<O> listItems;

    /**
     * Variável interna para controlar se o adapter está com icone de loading sendo exibido.
     */
    private boolean loading;

    public PaginationAdapter(Context context, List<O> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_PROGRESS:
                    L.d(TAG, "Type Progress criado");
                view = LayoutInflater.from(context).inflate(R.layout.partial_progress_pagination,
                        parent, false);
                holder = new ProgressViewHolder(view);
                break;
            case TYPE_ITEM:
                L.d(TAG, "Type Item criado");
                int itemId = getLayoutIdForItem();
                view = LayoutInflater.from(context).inflate(itemId, parent, false);
                holder = createViewHolderForItem(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listItems.size() && isLoadingItemShowing()) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (this.listItems != null && isLoadingItemShowing()) {
            return listItems.size() + 1;
        } else if (this.listItems != null && !isLoadingItemShowing()) {
            return this.listItems.size();
        }
        return 0;
    }

    public boolean isLoadingItemShowing() {
        return loading;
    }

    public void addLoadingItem() {
        this.loading = true;
        notifyDataSetChanged();
    }

    public void removeLoadingItem() {
        this.loading = false;
        notifyDataSetChanged();
    }

    public abstract int getLayoutIdForItem();

    public abstract RecyclerView.ViewHolder createViewHolderForItem(View view);

    public void removeLoadingAndAddItems(List<O> newList) {
        this.loading = false;
        listItems.addAll(newList);
        notifyDataSetChanged();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
