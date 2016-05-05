package br.com.luizfp.segredosufsc.domain;

import br.com.luizfp.segredosufsc.util.L;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a {@link rx.Subscriber}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase {

  private static final String TAG = UseCase.class.getSimpleName();
  private final Scheduler mSubscribeOnScheduler;
  private final Scheduler mObserveOnScheduler;

  private Subscription subscription = Subscriptions.empty();

  public UseCase(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler) {
    this.mSubscribeOnScheduler = subscribeOnScheduler;
    this.mObserveOnScheduler = observeOnScheduler;
  }

  /**
   * Builds an {@link rx.Observable} which will be used when executing the current {@link UseCase}.
   * Este método é chamado no execute, sendo assim, se você tiver um caso de uso que herde dessa
   * classe abstrata {@link UseCase} quando você criar esse caso de uso e chamar o execute e.g.:
   * new MyUseCase().execute(new MySubscriber()) o execute vai chamar o método do seu caso
   * (MyUseCase) que já retorna um Observable.
   */
  protected abstract Observable buildUseCaseObservable();

  /**
   * Executes the current use case.
   *
   * @param useCaseSubscriber The guy who will be listen to the observable build
   * with {@link #buildUseCaseObservable()}.
   */
  @SuppressWarnings("unchecked")
  public void execute(Subscriber useCaseSubscriber) {
    L.d(TAG, "execute()");
    this.subscription = this.buildUseCaseObservable()
        .subscribeOn(mSubscribeOnScheduler)
        .observeOn(mObserveOnScheduler)
        .subscribe(useCaseSubscriber);
  }

  /**
   * Unsubscribes from current {@link rx.Subscription}.
   */
  public void unsubscribe() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}