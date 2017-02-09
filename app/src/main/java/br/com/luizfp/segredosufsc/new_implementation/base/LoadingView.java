package br.com.luizfp.segredosufsc.new_implementation.base;

/**
 * Created by luiz on 8/4/16.
 */
public interface LoadingView extends MvpView {
    void showLoading();
    void hideLoading();
    void showError(String message);
}