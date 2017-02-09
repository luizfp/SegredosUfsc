package br.com.luizfp.segredosufsc.segredo;

import android.content.Context;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.Usuario;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.new_implementation.segredo.Secret;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.TokenUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/17/16.
 */
public class SendSecretUseCase extends UseCase {

    private final String mSegredo;
    private final Context mContext;

    public SendSecretUseCase(Scheduler subscribeOnScheduler,
                             Scheduler observeOnScheduler, Context context, String segredo) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mContext = context;
        this.mSegredo = segredo;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    //Thread.sleep(5000);
                    Credentials credentials = TokenUtils.getCredentials(mContext);
                    Secret secret = new Secret();
                    secret.setSegredo(mSegredo);
                    Usuario usuario = new Usuario();
                    usuario.setId(credentials.getIdUser());
                    secret.setUsuario(usuario);
                    RestClient
                            .createService(SegredosRest.class, credentials)
                            .insert(secret)
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
