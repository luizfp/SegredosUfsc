package br.com.luizfp.segredosufsc.segredo.novo;

import br.com.luizfp.segredosufsc.mvp.Presenter;

/**
 * Created by luiz on 3/17/16.
 */
public interface NovoSegredoActivityPresenter extends Presenter<NovoSegredoActivityView> {
    void sendNewSecret(String secret);
}
