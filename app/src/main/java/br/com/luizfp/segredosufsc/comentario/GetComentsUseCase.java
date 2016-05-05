package br.com.luizfp.segredosufsc.comentario;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.segredo.SegredosRest;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.TimeUtils;
import br.com.luizfp.segredosufsc.util.TokenUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/19/16.
 */
public class GetComentsUseCase extends UseCase {

    private final Context mContext;
    private final Long mIdSegredo;

    public GetComentsUseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler,
                             Context context, Long idSegredo) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mContext = context;
        this.mIdSegredo = idSegredo;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
//                    if (BuildConfig.DEBUG)
//                        Thread.sleep(5000);
                    Credentials credentials = TokenUtils.getCredentials(mContext);
                    RestClient
                            .createService(SegredosRest.class, credentials)
                            .getAllComentsBySegredo(mIdSegredo)
                            .map(originalList -> {
                                List<ComentarioViewModel> comentarioViewModelList = new ArrayList<>();
                                for (Comentario comentario : originalList) {
                                    ComentarioViewModel c = new ComentarioViewModel();
                                    c.setComentario(comentario.getComentario());
                                    c.setDataString(TimeUtils.toUserFriendlyTimestamp(comentario.getData()));
                                    c.setInicialNomeUsuario(comentario.getUsuario().getInicial());
                                    c.setNomeCursoUsuario(comentario.getUsuario().getCurso().getNome());
                                    comentarioViewModelList.add(c);
                                }
                                return comentarioViewModelList;
                            })
                            .subscribe(new Observer<List<ComentarioViewModel>>() {
                                @Override
                                public void onCompleted() {
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(List<ComentarioViewModel> comentariosList) {
                                    subscriber.onNext(comentariosList);
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
