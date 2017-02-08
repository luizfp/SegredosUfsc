package br.com.luizfp.segredosufsc.segredo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.ui.pagination.PaginationAdapter;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.TypefaceHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luiz on 2/23/16.
 */
public class SegredosAdapter extends PaginationAdapter<SegredoViewModel> {
    private static final String TAG = SegredosAdapter.class.getSimpleName();
    private final SegredoOnClickListener callback;
    private final int bgColorsArray[] = new int[] {R.color.segredo_bg_1, R.color.segredo_bg_2,
            R.color.segredo_bg_3, R.color.segredo_bg_4, R.color.segredo_bg_5, R.color.segredo_bg_6,
            R.color.segredo_bg_7, R.color.segredo_bg_8};
    private final Typeface typefaceSecret;
    private final Typeface typefaceMinorInfos;
    private int colorsPosition;

    public interface SegredoOnClickListener {
        void onClickItem(int position, TextView txtSegredo);
        void onClickToFavorite(int position, boolean addToFavorite);
        void onClickToSeeComents(int position);
        void onClickToShare(int position, View shareView, View layoutSegredo);
    }

    public SegredosAdapter(Context context, List<SegredoViewModel> segredoList,
                           SegredoOnClickListener callback) {
        super(context, segredoList);
        this.callback = callback;
        typefaceSecret = TypefaceHelper.get(context, "ubuntu_light.ttf");
        typefaceMinorInfos = TypefaceHelper.get(context, "josefin_sans_semi_bold.ttf");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder genericHolder, final int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            SegredosViewHolder holder = (SegredosViewHolder) genericHolder;
            SegredoViewModel segredo = listItems.get(position);
            holder.txtData.setText(segredo.getDataString());
            holder.txtData.setTypeface(typefaceMinorInfos);
            holder.txtSegredo.setText(segredo.getSegredo());
            holder.txtSegredo.setTypeface(typefaceSecret);
            holder.txtNumComents.setText(String.valueOf(segredo.getComentarioList().size()));
            holder.txtNumComents.setTypeface(typefaceMinorInfos);
            holder.txtNumFavorites.setText(String.valueOf(segredo.getTotalFavorites()));
            holder.txtNumFavorites.setTypeface(typefaceMinorInfos);
            Curso curso = segredo.getUsuario().getCurso();
            holder.txtCurso.setText(curso.getNome());
            holder.txtCurso.setTypeface(typefaceMinorInfos);
            // Se ainda não tiver uma cor, seta uma
            if (segredo.getBackgroundColor() == 0)
                segredo.setBackgroundColor(getColor());
            holder.layoutSegredo.setBackgroundColor(segredo.getBackgroundColor());
            // Ao favoritar (ou desfavoritar) um segredo, é alterado esse atributo na lista de
            // segredos do presenter, que é a mesma do adapter (mesma referência na memória),
            // assim, vai ser setado certo na próxima vez que se desenhar esse segredo, mesmo
            // ele sendo reciclado (recycler pool).
            holder.likeButtonFavorite.setLiked(segredo.isFavorite());
        }
    }

    @Override
    public int getLayoutIdForItem() {
        return R.layout.item_segredo;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolderForItem(View view) {
        return new SegredosViewHolder(view, callback);
    }

    private int getColor() {
        if (colorsPosition >= bgColorsArray.length) {
            colorsPosition = 0;
        }
        int color = ContextCompat.getColor(context, bgColorsArray[colorsPosition]);
        colorsPosition ++;
        return color;
    }

    public static class SegredosViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, OnLikeListener {
        @BindView(R.id.txt_data) TextView txtData;
        @BindView(R.id.txt_segredo) TextView txtSegredo;
        @BindView(R.id.txt_numComents) TextView txtNumComents;
        @BindView(R.id.txt_curso) TextView txtCurso;
        @BindView(R.id.layout_segredo) ViewGroup layoutSegredo;
        @BindView(R.id.likeButton_favorite) LikeButton likeButtonFavorite;
        @BindView(R.id.txt_numFavorites) TextView txtNumFavorites;

        private SegredoOnClickListener callback;

        public SegredosViewHolder(View view, SegredoOnClickListener callbak) {
            super(view);
            this.callback = callbak;
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            likeButtonFavorite.setOnLikeListener(this);
        }

        @OnClick(R.id.imgBtn_share)
        public void onClickToShare(View shareView) {
            L.d(TAG, "onClickToShare()");
            // FIXME: Verifacar o adapter position pois se o usuário clicar
            // para ver os comentários de um segredo que está sendo removido dos favoritos,
            // por exemplo, o index passado será -1 e teremos uma exceção
            if (callback != null && getAdapterPosition() >= 0) {
                callback.onClickToShare(getAdapterPosition(), shareView, layoutSegredo);
            }
        }

        @OnClick(R.id.layout_coments)
        public void onClickToSeeComents(View view) {
            L.d(TAG, "onClickToSeeComents()");
            if (callback != null && getAdapterPosition() >= 0) {
                callback.onClickToSeeComents(getAdapterPosition());
            }
        }

        @Override
        public void liked(LikeButton likeButton) {
            L.d(TAG, "onClickToFavorite()");
            if (callback != null && getAdapterPosition() >= 0) {
                callback.onClickToFavorite(getAdapterPosition(), true);
            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            L.d(TAG, "onClickToFavorite()");
            if (callback != null && getAdapterPosition() >= 0) {
                callback.onClickToFavorite(getAdapterPosition(), false);
            }
        }

        @Override
        public void onClick(View v) {
            L.d(TAG, "onClickItem()");
            if (callback != null && getAdapterPosition() >= 0) {
                callback.onClickItem(getAdapterPosition(), txtSegredo);
            }
        }
    }
}