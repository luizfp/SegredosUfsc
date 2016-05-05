package br.com.luizfp.segredosufsc.mvp;

/**
 * Created by luiz on 3/9/16.
 */
public interface LoadingView extends MvpView {
    void showLoading();
    void hideLoading();
    void showError(String errorMessage);

}
