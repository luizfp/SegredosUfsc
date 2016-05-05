package br.com.luizfp.segredosufsc.segredo.activity;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.domain.DefaultSubscriber;
import br.com.luizfp.segredosufsc.UpdateUserLetterUseCase;
import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.event.SwipeToFirstTabEvent;
import br.com.luizfp.segredosufsc.exceptions.DefaultErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorMessageFactory;
import br.com.luizfp.segredosufsc.mvp.MvpPresenter;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.Prefs;
import br.com.luizfp.segredosufsc.util.RxUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luiz on 3/15/16.
 */
@SuppressWarnings("ConstantConditions")
public class SegredosActivityPresenterImpl extends MvpPresenter<SegredosActivityView>
        implements SegredosActivityPresenter {

    private static final String TAG = SegredosActivityPresenterImpl.class.getSimpleName();
    private UseCase mUpdateUserLetterUseCase;
    private char mCurrentLetter;

    public SegredosActivityPresenterImpl(SegredosActivityView segredosActivityView) {
        attachView(segredosActivityView);
    }

    @Override
    public void onUiCreated(boolean isFirstCreation) {
        setIsFirstUiCreation(isFirstCreation);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        detachView();
        RxUtils.unsubscribeIfNotNull(mUpdateUserLetterUseCase);
    }

    @Override
    public void onLetterChanged(char newLetter) {
        // TODO: mCurrentLetter deve existir apenas no presenter e não na view
        this.mCurrentLetter = newLetter;
        if (isViewAttached()) {
            getView().changeLetterInMenu(newLetter);
            mUpdateUserLetterUseCase = new UpdateUserLetterUseCase(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread(),
                    getView().context().getApplicationContext(),
                    newLetter);
            mUpdateUserLetterUseCase.execute(new UpdateUserLetterSubscriber());
        }
    }

    /**
     * Chamado quando a view mostrando que não há nenhum favorito é exibida e o usuário
     * clica no botão para começar adicionar favoritos.
     *
     * @param event um evento {@link SwipeToFirstTabEvent}
     */
    @Subscribe
    public void onNeedToSwipeToFirstTab(SwipeToFirstTabEvent event) {
        L.d(TAG, "Evento para fazer o swipe recebido");
        if (isViewAttached())
            getView().swipeToFirstTab();
    }

    private void showError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView().showError(ErrorMessageFactory.create(getView().context(),
                    errorBundle.getException()));
        }
    }

    private class UpdateUserLetterSubscriber extends DefaultSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            super.onNext(response);
            if (response != null && response.getStatus().equals(Response.OK)) {
                L.d(TAG, response.getStatus());
                if (isViewAttached()) {
                    Prefs.putChar(getView().context(), Prefs.INICIAL_USER_KEY, mCurrentLetter);
                    getView().showLetterChangedWithSuccess(getView().context().getString(R.string.inicial_atualizada));
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao atualizar inicial do usuário", e);
            SegredosActivityPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }
}
