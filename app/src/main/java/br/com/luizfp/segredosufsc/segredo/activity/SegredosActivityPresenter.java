package br.com.luizfp.segredosufsc.segredo.activity;

import br.com.luizfp.segredosufsc.mvp.Presenter;

/**
 * Created by luiz on 3/15/16.
 */
public interface SegredosActivityPresenter extends Presenter<SegredosActivityView> {
    void onLetterChanged(char newLetter);
    void onStart();
    void onStop();
}
