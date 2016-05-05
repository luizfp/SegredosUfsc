package br.com.luizfp.segredosufsc.comentario;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.util.L;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by luiz on 2/27/16.
 */
public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder> {
    private static final String TAG = ComentariosAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<ComentarioViewModel> mComentariosList;
    private final ComentariosOnClickListener mCallbak;
    private final int mBgColorsArray[] = new int[] {R.color.segredo_bg_1, R.color.segredo_bg_2,
            R.color.segredo_bg_3, R.color.segredo_bg_4, R.color.segredo_bg_5, R.color.segredo_bg_6,
            R.color.segredo_bg_7, R.color.segredo_bg_8};
    private int mColorsPosition;

    public interface ComentariosOnClickListener {
        void onClickItem(int position, TextView txtComentario);
    }

    public ComentariosAdapter(Context context, List<ComentarioViewModel> comentariosList, ComentariosOnClickListener callbak) {
        this.mContext = context;
        this.mComentariosList = comentariosList;
        this.mCallbak = callbak;
    }

    @Override
    public ComentariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comentario,
                parent, false);
        return new ComentariosViewHolder(view, mCallbak);
    }

    @Override
    public void onBindViewHolder(ComentariosViewHolder holder, int position) {
        ComentarioViewModel comentario = mComentariosList.get(position);
        // Se ainda não tiver uma cor, seta uma
        if (comentario.getBackgroundColorInicialUsuario() == 0)
            comentario.setBackgroundColorInicialUsuario(getColor());
        holder.mLayoutInicialNome.setBackgroundColor(comentario.getBackgroundColorInicialUsuario());
        holder.mTxtInicial.setText(String.valueOf(comentario.getInicialNomeUsuario()));
        holder.mTxtComentario.setText(comentario.getComentario());
        holder.mTxtData.setText(comentario.getDataString());
        holder.mTxtCurso.setText(comentario.getNomeCursoUsuario());
    }

    @Override
    public int getItemCount() {
        return this.mComentariosList != null ? this.mComentariosList.size() : 0;
    }

    public void setNewList(List<ComentarioViewModel> comentarioList) {
        mComentariosList.clear();
        mComentariosList.addAll(comentarioList);
        notifyDataSetChanged();
    }

    private int getColor() {
        if (mColorsPosition >= mBgColorsArray.length) {
            mColorsPosition = 0;
        }
        int color = ContextCompat.getColor(mContext, mBgColorsArray[mColorsPosition]);
        mColorsPosition++;
        return color;
    }

    public static class ComentariosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txt_comentario) TextView mTxtComentario;
        @Bind(R.id.txt_inicial_nome) TextView mTxtInicial;
        @Bind(R.id.txt_data) TextView mTxtData;
        @Bind(R.id.layout_inicial_nome) ViewGroup mLayoutInicialNome;
        @Bind(R.id.txt_curso) TextView mTxtCurso;

        private ComentariosOnClickListener callback;

        public ComentariosViewHolder(View view, ComentariosOnClickListener callbak) {
            super(view);
            this.callback = callbak;
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        // TODO: Verificar se para diferentes segredos o mTxtComentario passado será o certo
        // que contém o o texto do comentario clicado. Mesma coisa para o txtSegredo.
        @Override
        public void onClick(View v) {
            L.d(TAG, "onClickItem()");
            if (callback != null) {
                callback.onClickItem(getAdapterPosition(), mTxtComentario);
            }
        }

    }
}
