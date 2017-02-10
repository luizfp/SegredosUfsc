package br.com.luizfp.segredosufsc.new_implementation.internal.di;

import javax.inject.Singleton;

import br.com.luizfp.segredosufsc.new_implementation.SegredosUfscApplication;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.module.ApplicationModule;
import br.com.luizfp.segredosufsc.new_implementation.secret.data.SecretsRepository;
import dagger.Component;

/**
 * This is a Dagger component. Refer to {@link SegredosUfscApplication} for the list of Dagger components
 * used in this application.
 * <P>
 * Even though Dagger allows annotating a {@link Component @Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in {@link
 * SegredosUfscApplication}.
 */
@Singleton
@Component(modules = {SecretsRepositoryModule.class, ApplicationModule.class})
public interface SecretsRepositoryComponent {

    SecretsRepository getTasksRepository();
}
