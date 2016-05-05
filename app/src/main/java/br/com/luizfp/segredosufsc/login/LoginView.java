package br.com.luizfp.segredosufsc.login;

import br.com.luizfp.segredosufsc.mvp.LoadingView;

/**
 * Created by luiz on 3/6/16.
 */
public interface LoginView extends LoadingView {
    void showNotAnEmailUfscError(String errorMessage);
    void navigateToConfirmAccessCode(Long idCodigoAcesso, char supostaInicialNome);
    void startLogoAnimation();
    void changeAnimationToFinalState();
    void emailSendWithSuccess();
}
