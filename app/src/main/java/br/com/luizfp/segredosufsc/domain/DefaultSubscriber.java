package br.com.luizfp.segredosufsc.domain;

import br.com.luizfp.segredosufsc.util.L;

/**
 * Default subscriber base class to be used whenever you want default error handling.
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {

  private static final String TAG = DefaultSubscriber.class.getSimpleName();

  @Override
  public void onCompleted() {
    L.d(TAG, "onCompleted()");
    // no-op by default.
  }

  @Override
  public void onError(Throwable e) {
    L.d(TAG, "onError()");
    // no-op by default.
  }

  @Override
  public void onNext(T t) {
    L.d(TAG, "onNext()");
    // no-op by default.
  }
}