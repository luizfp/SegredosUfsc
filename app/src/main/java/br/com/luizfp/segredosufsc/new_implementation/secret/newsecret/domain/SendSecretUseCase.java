package br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.domain;

import javax.inject.Inject;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.interactor.UseCase;
import br.com.luizfp.segredosufsc.new_implementation.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */

public class SendSecretUseCase extends UseCase<SendSecretUseCase.Params, Response> {

    @Inject
    public SendSecretUseCase(BaseSchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    @Override
    public Observable<Response> buildUseCaseObservable(Params params) {
        return null;
    }

    public static final class Params {

        private final String secret;

        private Params(String secret) {
            this.secret = secret;
        }

        public static Params forSecret(String secret) {
            return new Params(secret);
        }
    }
}