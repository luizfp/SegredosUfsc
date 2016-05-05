package br.com.luizfp.segredosufsc.util;

import br.com.luizfp.segredosufsc.domain.UseCase;

/**
 * Created by luiz on 3/7/16.
 */
public class RxUtils {

    public static void unsubscribeIfNotNull(UseCase useCase) {
        if (useCase != null) {
            useCase.unsubscribe();
        }
    }
}
