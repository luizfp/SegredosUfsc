package br.com.luizfp.segredosufsc.comentario;

import android.content.Context;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.Usuario;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.TokenUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/17/16.
 */
public class SendCommentUseCase extends UseCase {

    private final Context mContext;
    private final String mComentario;
    private final Long mIdSegredo;

    public SendCommentUseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler,
                              Context context, String comentario, Long idSegredo) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mContext = context;
        this.mComentario = comentario;
        this.mIdSegredo = idSegredo;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    //if (BuildConfig.DEBUG)
                        //Thread.sleep(5000);
                    Credentials credentials = TokenUtils.getCredentials(mContext);
                    Comentario comentario = new Comentario();
                    comentario.setComentario(mComentario);
                    comentario.setIdSegredo(mIdSegredo);
                    Usuario usuario = new Usuario();
                    usuario.setId(credentials.getIdUser());
                    comentario.setUsuario(usuario);
                    RestClient
                            .createService(ComentariosRest.class, credentials)
                            .insert(comentario)
                            .subscribe(new Observer<Response>() {
                                @Override
                                public void onCompleted() {
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(Response response) {
                                    subscriber.onNext(response);
                                }
                            });
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            } else {
                subscriber.onError(new NoNetworkConnectionException());
            }
        });
    }
}
