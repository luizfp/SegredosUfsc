package br.com.luizfp.segredosufsc.segredo.activity;

import br.com.luizfp.segredosufsc.mvp.MvpView;

/**
 * Created by luiz on 3/15/16.
 */
public interface SegredosActivityView extends MvpView {
    void changeLetterInMenu(char newLetter);
    void showError(String errorMessage);
    void swipeToFirstTab();
    void showLetterChangedWithSuccess(String message);
}
