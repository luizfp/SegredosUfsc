package br.com.luizfp.segredosufsc.new_implementation.segredo.newsecret.presentation;

import br.com.luizfp.segredosufsc.new_implementation.base.LoadingView;
import br.com.luizfp.segredosufsc.new_implementation.base.MvpPresenter;

/**
 * Created by luiz on 09/02/17.
 */

public interface NewSecretContract {

    interface View extends LoadingView {

    }

    interface Presenter extends MvpPresenter<View> {
        void sendSecret(String segredo);
    }
}