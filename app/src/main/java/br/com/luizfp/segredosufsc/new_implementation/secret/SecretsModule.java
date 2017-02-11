/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.luizfp.segredosufsc.new_implementation.secret;

import android.support.annotation.NonNull;

import br.com.luizfp.segredosufsc.new_implementation.internal.di.PerActivity;
import br.com.luizfp.segredosufsc.new_implementation.schedulers.BaseSchedulerProvider;
import br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.domain.SendSecretUseCase;
import br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.presentation.NewSecretContract;
import br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.presentation.NewSecretPresenter;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class SecretsModule {

  @Provides
  @PerActivity
  NewSecretContract.Presenter provideNewSecretPresenter(@NonNull SendSecretUseCase sendSecretUseCase) {
    return new NewSecretPresenter(sendSecretUseCase);
  }

  @Provides
  @PerActivity
  SendSecretUseCase provideSendSecretUseCase(@NonNull BaseSchedulerProvider schedulerProvider) {
    return new SendSecretUseCase(schedulerProvider);
  }
}