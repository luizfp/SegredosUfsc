package br.com.luizfp.segredosufsc.segredo.fragment;

import java.util.List;

import br.com.luizfp.segredosufsc.mvp.Presenter;
import br.com.luizfp.segredosufsc.comentario.ComentarioViewModel;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;

/**
 * Created by luiz on 3/15/16.
 */
public interface SegredosFragmentPresenter extends Presenter<SegredosFragmentView> {
    void onClickToFavorite(int position, boolean addToFavorite);
    void onNewDataNeeded(long offset, boolean pagination);
    SegredoViewModel getSegredo(int position);
    String getTipo();
    void setTipo(String tipo);
    void onStart();
    void onStop();
    void onNeedToSwipeToFirstTab();
    void updateNumberOfComments(Long idSegredo, List<ComentarioViewModel> newCommentsList);
}
