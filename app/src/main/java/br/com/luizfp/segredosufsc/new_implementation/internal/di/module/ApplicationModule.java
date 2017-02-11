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
package br.com.luizfp.segredosufsc.new_implementation.internal.di.module;

import android.content.Context;

import javax.inject.Singleton;

import br.com.luizfp.segredosufsc.new_implementation.schedulers.BaseSchedulerProvider;
import br.com.luizfp.segredosufsc.new_implementation.schedulers.SchedulerProvider;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {
  private final Context mContext;

  public ApplicationModule(Context context) {
    mContext = context;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return mContext;
  }

  @Provides
  @Singleton
  BaseSchedulerProvider provideSchedulerProvider() {
    return new SchedulerProvider();
  }
}