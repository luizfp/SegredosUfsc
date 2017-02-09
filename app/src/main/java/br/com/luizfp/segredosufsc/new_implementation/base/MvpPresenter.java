package br.com.luizfp.segredosufsc.new_implementation.base;

/**
 * Created by luiz on 09/02/17.
 */

public interface MvpPresenter<V extends MvpView> {

    void attachView(V view);

    void detachView();

    /**
     * Proper method to cancel pending request &&/|| release resources
     */
    void destroy();
}