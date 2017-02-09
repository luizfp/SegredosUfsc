package br.com.luizfp.segredosufsc.new_implementation.segredo.novo;

import android.support.annotation.Nullable;

/**
 * Created by luiz on 09/02/17.
 */

public class NovoSegredoPresenter implements NovoSegredoContract.Presenter {
    @Nullable
    private NovoSegredoContract.View mView;

    @Override
    public void attachView(NovoSegredoContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void sendSecret(String segredo) {

    }
}