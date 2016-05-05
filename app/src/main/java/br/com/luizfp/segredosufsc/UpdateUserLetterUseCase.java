package br.com.luizfp.segredosufsc;

import android.content.Context;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.network.UsuarioRest;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.TokenUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/15/16.
 */
public class UpdateUserLetterUseCase extends UseCase {

    private static final String TAG = UpdateUserLetterUseCase.class.getSimpleName();
    private final Context mContext;
    private final char mNewLetter;

    public UpdateUserLetterUseCase(
            Scheduler subscribeOnScheduler,
            Scheduler observeOnScheduler,
            Context context,
            char newLetter) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mContext = context;
        this.mNewLetter = newLetter;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    Credentials credentials = TokenUtils.getCredentials(mContext);
                    Usuario usuario = new Usuario();
                    usuario.setId(credentials.getIdUser());
                    usuario.setInicial(mNewLetter);
                    RestClient
                            .createService(UsuarioRest.class, credentials)
                            .updateInicial(usuario)
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
