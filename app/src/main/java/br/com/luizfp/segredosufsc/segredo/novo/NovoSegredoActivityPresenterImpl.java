package br.com.luizfp.segredosufsc.segredo.novo;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.luizfp.segredosufsc.domain.DefaultSubscriber;
import br.com.luizfp.segredosufsc.segredo.SendSecretUseCase;
import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.exceptions.DefaultErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.DefaultMessageException;
import br.com.luizfp.segredosufsc.exceptions.ErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorMessageFactory;
import br.com.luizfp.segredosufsc.mvp.MvpPresenter;
import br.com.luizfp.segredosufsc.util.RxUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luiz on 3/17/16.
 */
@SuppressWarnings("ConstantConditions")
public class NovoSegredoActivityPresenterImpl extends MvpPresenter<NovoSegredoActivityView>
        implements NovoSegredoActivityPresenter {

    private static final String TAG = NovoSegredoActivityPresenterImpl.class.getSimpleName();
    private UseCase mSendSecretUseCase;
    private boolean mNovoSegredoFoiEnviado;

    public NovoSegredoActivityPresenterImpl(NovoSegredoActivityView novoSegredoActivityView) {
        attachView(novoSegredoActivityView);
    }

    @Override
    public void onUiCreated(boolean isFirstCreation) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        detachView();
        RxUtils.unsubscribeIfNotNull(mSendSecretUseCase);
    }

    @Override
    public void sendNewSecret(String secret) {
        if (isViewAttached()) {
            getView().showHideNovoSegredoFragment();
            getView().addLoadingFragment();
            getView().showLoadingToolbarMessage();
            mSendSecretUseCase = new SendSecretUseCase(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread(),
                    getView().context(),
                    secret);
            mSendSecretUseCase.execute(new SendSegredoSubscriber());
        }
    }

    // Envia evento para o fragment loading para ele parar a animação
    private void segredoEnviado() {
        mNovoSegredoFoiEnviado = true;
        EventBus.getDefault().post(new SecretSuccessInsertEvent());
        if (isViewAttached()) {
            getView().showSuccessToolbarMessage();
        }
    }

    private void showError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView().removeLoadingFragment();
            getView().showHideNovoSegredoFragment();
            getView().resetToolbarMessage();
            getView().showError(ErrorMessageFactory.create(getView().context(), errorBundle.getException()));
        }
    }

    public static class SecretSuccessInsertEvent {}

    private class SendSegredoSubscriber extends DefaultSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            super.onNext(response);
            if (response != null && response.getStatus().equals(Response.OK)) {
                NovoSegredoActivityPresenterImpl.this.segredoEnviado();
            } else {
                NovoSegredoActivityPresenterImpl.this.showError(
                        new DefaultErrorBundle(
                                new DefaultMessageException("Erro ao enviar seu segredo, tente novamente")));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao enviar segredo", e);
            NovoSegredoActivityPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }
}
