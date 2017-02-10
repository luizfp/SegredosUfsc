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
package br.com.luizfp.segredosufsc.new_implementation.internal.di.component;

import br.com.luizfp.segredosufsc.new_implementation.internal.di.PerActivity;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.module.ActivityModule;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.module.SecretModule;
import br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.presentation.NewSecretFragment;
import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SecretModule.class})
public interface SecretComponent extends ActivityComponent {
  void inject(NewSecretFragment newSecretFragment);
}