package br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.interactor.DefaultObserver;
import br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.domain.SendSecretUseCase;

import static br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.domain.SendSecretUseCase.*;

/**
 * Created by luiz on 09/02/17.
 */

public class NewSecretPresenter implements NewSecretContract.Presenter {
    @NonNull
    private final SendSecretUseCase mSendSecretUseCase;
    @Nullable
    private NewSecretContract.View mView;

    public NewSecretPresenter(@NonNull SendSecretUseCase sendSecretUseCase) {
        mSendSecretUseCase = sendSecretUseCase;
    }

    @Override
    public void attachView(NewSecretContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void destroy() {
        mSendSecretUseCase.dispose();
    }

    @Override
    public void sendSecret(String segredo) {
        mSendSecretUseCase.execute(Params.forSecret(segredo), new SendSecretObserver());
    }

    private class SendSecretObserver extends DefaultObserver<Response> {

        @Override
        public void onNext(Response response) {
            super.onNext(response);
        }

        @Override
        public void onComplete() {
            super.onComplete();
        }

        @Override
        public void onError(Throwable exception) {
            super.onError(exception);
        }
    }
}