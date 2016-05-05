package br.com.luizfp.segredosufsc.login;

import android.content.Context;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import br.com.luizfp.segredosufsc.util.L;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/7/16.
 */
public class SendEmailUseCase extends UseCase {

    private static final String TAG = SendEmailUseCase.class.getSimpleName();
    private final String mEmail;
    private final Context mContext;

    public SendEmailUseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler,
                            Context context, String email) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mEmail = email;
        this.mContext = context.getApplicationContext();
    }

    @SuppressWarnings("AccessStaticViaInstance")
    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    // We are good to go!
                    // FIXME: O retrofit já executa em outra thread, essa mesma criada para esse
                    // observable
                    RestClient
                            .createService(LoginRest.class)
                            .sendEmail(mEmail)
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
                L.d(TAG, "Sem internet");
                subscriber.onError(new NoNetworkConnectionException());
            }
        });
    }
}
