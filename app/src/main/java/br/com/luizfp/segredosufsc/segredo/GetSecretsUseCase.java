package br.com.luizfp.segredosufsc.segredo;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.comentario.Comentario;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.comentario.ComentarioViewModel;
import br.com.luizfp.segredosufsc.new_implementation.segredo.Secret;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.TimeUtils;
import br.com.luizfp.segredosufsc.util.TokenUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/15/16.
 */
public class GetSecretsUseCase extends UseCase {

    private Context mContext;
    private int mLimit;
    private long mOffset;

    public GetSecretsUseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler) {
        super(subscribeOnScheduler, observeOnScheduler);
    }

    public GetSecretsUseCase init(Context context, int limit, long offset) {
        this.mContext = context;
        this.mLimit = limit;
        this.mOffset = offset;

        return this;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    //Thread.sleep(5000);
                    Credentials credentials = TokenUtils.getCredentials(mContext);
                    RestClient
                            .createService(SegredosRest.class, credentials)
                            .getList(credentials.getIdUser(), mLimit, mOffset)
                            .map(segredoList -> {
                                // TODO: Melhor fazer dois DataMapper separados para isso
                                List<SegredoViewModel> l = new ArrayList<>();
                                for (Secret secret : segredoList) {
                                    SegredoViewModel s = new SegredoViewModel();
                                    s.setSegredo("\"" + secret.getSegredo() + "\"");
                                    List<ComentarioViewModel> comentarioViewModelList = new ArrayList<>();
                                    for (Comentario comentario : secret.getComentarioList()) {
                                        ComentarioViewModel c = new ComentarioViewModel();
                                        c.setComentario(comentario.getComentario());
                                        c.setDataString(TimeUtils.toUserFriendlyTimestamp(comentario.getData()));
                                        c.setInicialNomeUsuario(comentario.getUsuario().getInicial());
                                        c.setNomeCursoUsuario(comentario.getUsuario().getCurso().getNome());
                                        comentarioViewModelList.add(c);
                                    }
                                    s.setComentarioList(comentarioViewModelList);
                                    s.setDataString(TimeUtils.toUserFriendlyTimestamp(secret.getData()));
                                    s.setId(secret.getId());
                                    s.setUsuario(secret.getUsuario());
                                    s.setFavorite(secret.isFavorite());
                                    s.setTotalFavorites(secret.getTotalFavorites());
                                    l.add(s);
                                }
                                return l;
                            })
                            .subscribe(new Observer<List<SegredoViewModel>>() {
                                @Override
                                public void onCompleted() {
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(List<SegredoViewModel> segredos) {
                                    subscriber.onNext(segredos);
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
