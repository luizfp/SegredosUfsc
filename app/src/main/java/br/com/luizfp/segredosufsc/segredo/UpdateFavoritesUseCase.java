package br.com.luizfp.segredosufsc.segredo;

import android.content.Context;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.TokenUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/20/16.
 */
public class UpdateFavoritesUseCase extends UseCase {

    private Context mContext;
    private boolean mAddToFavorite;
    private Long mIdSegredo;

    public UpdateFavoritesUseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler) {
        super(subscribeOnScheduler, observeOnScheduler);
    }

    public void init(Context context, Long idSegredo, boolean addToFavorite) {
        this.mContext = context;
        this.mAddToFavorite = addToFavorite;
        this.mIdSegredo = idSegredo;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    Credentials credentials = TokenUtils.getCredentials(mContext);
                    SegredoFavoritoUsuario favoritoUsuario = new SegredoFavoritoUsuario();
                    favoritoUsuario.setIdSegredo(mIdSegredo);
                    favoritoUsuario.setIdUsuario(credentials.getIdUser());
                    favoritoUsuario.setAddToFavorite(mAddToFavorite);
                    RestClient
                            .createService(SegredosRest.class, credentials)
                            .addRemoveFavorite(favoritoUsuario)
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
