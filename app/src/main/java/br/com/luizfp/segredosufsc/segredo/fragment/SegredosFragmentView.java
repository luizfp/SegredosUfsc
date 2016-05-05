package br.com.luizfp.segredosufsc.segredo.fragment;

import java.util.List;

import br.com.luizfp.segredosufsc.mvp.LoadingView;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;

/**
 * Created by luiz on 3/15/16.
 */
public interface SegredosFragmentView extends LoadingView {
    void showData(List<SegredoViewModel> segredosList);
    void showPaginationLoading();
    void loadingNewItems();
    void removeSecretFromFavoritesTab(int position);
    void addSecretToFavoritesTab(int position);
    void showMessage(String message, boolean toast);
    void redrawList();
}
