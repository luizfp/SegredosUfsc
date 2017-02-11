package br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.interactor.DefaultObserver;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.PerActivity;
import br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.domain.SendSecretUseCase;

import static br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.domain.SendSecretUseCase.Params;

/**
 * Created by luiz on 09/02/17.
 */
@PerActivity
public class NewSecretPresenter implements NewSecretContract.Presenter {
    @NonNull
    private final SendSecretUseCase mSendSecretUseCase;
    @Nullable
    private NewSecretContract.View mView;

    @Inject
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