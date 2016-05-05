package br.com.luizfp.segredosufsc.login;

import br.com.luizfp.segredosufsc.mvp.Presenter;

/**
 * Created by luiz on 3/6/16.
 */
public interface LoginPresenter extends Presenter<LoginView> {
    void verifyEmail(String email);
}
