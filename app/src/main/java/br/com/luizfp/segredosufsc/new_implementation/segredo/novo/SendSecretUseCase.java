package br.com.luizfp.segredosufsc.new_implementation.segredo.novo;

import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.new_implementation.interactor.UseCase;
import br.com.luizfp.segredosufsc.new_implementation.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;

/**
 * Created by luiz on 09/02/17.
 */

public class SendSecretUseCase extends UseCase<String, Response> {


    SendSecretUseCase(BaseSchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    @Override
    public Observable<Response> buildUseCaseObservable(String s) {
        return null;
    }
}