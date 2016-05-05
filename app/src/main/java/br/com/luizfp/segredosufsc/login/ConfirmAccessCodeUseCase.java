package br.com.luizfp.segredosufsc.login;

import android.content.Context;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/10/16.
 */
public class ConfirmAccessCodeUseCase extends UseCase {

    private final Context mContext;
    private final Long mIdCodigoAcesso;
    private final String mCodigoAcesso;
    private final char mLetterSelected;
    private final Long mIdCourseSelected;

    public ConfirmAccessCodeUseCase( Scheduler subscribeOnScheduler, Scheduler observeOnScheduler,
                                     Context mContext, Long mIdCodigoAcesso, String mCodigoAcesso,
                                     char mLetterSelected, Long mCourseSelected) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mContext = mContext.getApplicationContext();
        this.mIdCodigoAcesso = mIdCodigoAcesso;
        this.mCodigoAcesso = mCodigoAcesso;
        this.mLetterSelected = mLetterSelected;
        this.mIdCourseSelected = mCourseSelected;
    }


    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    RestClient
                            .createService(LoginRest.class)
                            .confirmAccessCodeAndGenerateToken(mIdCodigoAcesso, mCodigoAcesso,
                                    mLetterSelected, mIdCourseSelected)
                            .subscribe(new Observer<Credentials>() {
                                @Override
                                public void onCompleted() {
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(Credentials credentials) {
                                    subscriber.onNext(credentials);
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
