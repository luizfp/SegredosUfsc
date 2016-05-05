package br.com.luizfp.segredosufsc.login;

import android.content.Context;

import java.util.List;

import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.exceptions.NoNetworkConnectionException;
import br.com.luizfp.segredosufsc.network.RestClient;
import br.com.luizfp.segredosufsc.util.ConnectionUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * Created by luiz on 3/11/16.
 */
public class GetCoursesUseCase extends UseCase {

    private final Context mContext;

    public GetCoursesUseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler,
                             Context context) {
        super(subscribeOnScheduler, observeOnScheduler);
        this.mContext = context;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            if (ConnectionUtils.isNetworkAvailable(mContext)) {
                try {
                    RestClient
                            .createService(LoginRest.class)
                            .getAllCourses()
                            .subscribe(new Observer<List<Curso>>() {
                                @Override
                                public void onCompleted() {
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(List<Curso> cursos) {
                                    subscriber.onNext(cursos);
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
