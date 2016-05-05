package br.com.luizfp.segredosufsc.login;

import android.util.Log;

import br.com.luizfp.segredosufsc.mvp.MvpPresenter;
import br.com.luizfp.segredosufsc.domain.DefaultSubscriber;
import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.exceptions.DefaultErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorMessageFactory;
import br.com.luizfp.segredosufsc.exceptions.MalformedEmailException;
import br.com.luizfp.segredosufsc.exceptions.NotAnUfscEmailException;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.PatternUtils;
import br.com.luizfp.segredosufsc.util.RxUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luiz on 3/6/16.
 */
@SuppressWarnings("ConstantConditions")
public class LoginPresenterImpl extends MvpPresenter<LoginView> implements LoginPresenter {

    private static final String TAG = LoginPresenterImpl.class.getSimpleName();
    private UseCase mSendEmailUseCase;
    private String mEmailUser;

    public LoginPresenterImpl(LoginView loginView) {
        attachView(loginView);
    }

    @Override
    public void verifyEmail(String email) {
        this.mEmailUser = email;
        if (PatternUtils.isThisEmailValid(email)) {
            if (email.contains("@grad.ufsc.br")) {
                if (isViewAttached()) {
                    getView().showLoading();
                    mSendEmailUseCase = new SendEmailUseCase(Schedulers.io(),
                            AndroidSchedulers.mainThread(), getView().context(), email);
                    mSendEmailUseCase.execute(new SendEmailSubscriber());
                }
            } else {
                showNotAnUfscEmailError(new DefaultErrorBundle(new NotAnUfscEmailException()));
            }
        } else {
            showError(new DefaultErrorBundle(new MalformedEmailException()));
        }

    }


    @Override
    public void onUiCreated(boolean isFirstCreation) {
        setIsFirstUiCreation(isFirstCreation);
    }

    @Override
    public void onResume() {
        if (isViewAttached()) {
            // Pois o usuário pode estar voltando da activity de confirmação do código e se não
            // resetar o estado do loading, no botão ainda mostrará: "EMAIL ENVIADO! :D" e não
            // permitirá novos clicks.
            getView().hideLoading();
            if (isFirstUiCreation())
                getView().startLogoAnimation();
            else
                getView().changeAnimationToFinalState();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        detachView();
        RxUtils.unsubscribeIfNotNull(mSendEmailUseCase);
    }

    private void showError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView().showError(ErrorMessageFactory.create(getView().context(), errorBundle.getException()));
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showNotAnUfscEmailError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView()
                    .showNotAnEmailUfscError(
                            ErrorMessageFactory
                                    .create(getView().context(), errorBundle.getException()));
        }
    }

    private void emailSendWithSuccess(Long idCodigoAcesso) {
        if (isViewAttached()) {
            getView().emailSendWithSuccess();

            char supostaInicial = mEmailUser != null ? mEmailUser.charAt(0) : 'L';
            L.d(TAG, "Suposta inicial enviada: " + supostaInicial);
            getView().navigateToConfirmAccessCode(idCodigoAcesso, supostaInicial);
        }
    }

    private class SendEmailSubscriber extends DefaultSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            super.onNext(response);
            if (response != null && response.getStatus().equals(Response.OK)) {
                L.d(TAG, "Email foi enviado com sucesso");
                LoginPresenterImpl.this.emailSendWithSuccess(response.getNumber());
            } else {
                Log.e(TAG, "Erro ao enviar o email");
                LoginPresenterImpl.this.showError(new DefaultErrorBundle(new Exception()));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao enviar email para usuário", e);
            LoginPresenterImpl.this.hideLoading();
            LoginPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }
}
