package br.com.luizfp.segredosufsc.comentario;

import br.com.luizfp.segredosufsc.mvp.Presenter;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;

/**
 * Created by luiz on 3/17/16.
 */
public interface ComentariosPresenter extends Presenter<ComentariosView> {
    void sendComent(String comentario);
    void setSegredo(SegredoViewModel segredo);
    SegredoViewModel getSegredo();
    void updateComentariosList();
    boolean thereIsANewNumberOfComents();
}
