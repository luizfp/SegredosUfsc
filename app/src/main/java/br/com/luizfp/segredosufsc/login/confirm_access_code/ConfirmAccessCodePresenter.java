package br.com.luizfp.segredosufsc.login.confirm_access_code;

import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.mvp.Presenter;

/**
 * Created by luiz on 3/9/16.
 */
public interface ConfirmAccessCodePresenter extends Presenter<ConfirmAccessCodeView> {
    void setIdCodigoAcesso(Long idCodigoAcesso);
    void setSelectedLetter(char newLetter);
    void verifyCode(String code, Curso curso);
}
