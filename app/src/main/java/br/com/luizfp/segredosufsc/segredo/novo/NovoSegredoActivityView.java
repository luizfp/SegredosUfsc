package br.com.luizfp.segredosufsc.segredo.novo;

import br.com.luizfp.segredosufsc.mvp.MvpView;

/**
 * Created by luiz on 3/17/16.
 */
public interface NovoSegredoActivityView extends MvpView {
    void addLoadingFragment();
    void removeLoadingFragment();
    void showHideNovoSegredoFragment();
    void showError(String errorMessage);
    void showLoadingToolbarMessage();
    void showSuccessToolbarMessage();
    void resetToolbarMessage();
}
