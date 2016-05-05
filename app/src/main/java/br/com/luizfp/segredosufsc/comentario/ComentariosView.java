package br.com.luizfp.segredosufsc.comentario;

import br.com.luizfp.segredosufsc.mvp.LoadingView;

/**
 * Created by luiz on 3/17/16.
 */
public interface ComentariosView extends LoadingView {
    void enableToResend();
    void enableToSendAnotherComent();
    void showSendWithSuccess(String message);
    void updateComentariosList();
    void redrawList();
}
