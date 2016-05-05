package br.com.luizfp.segredosufsc.segredo.fragment;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Iterator;
import java.util.List;

import br.com.luizfp.segredosufsc.domain.DefaultSubscriber;
import br.com.luizfp.segredosufsc.segredo.GetSecretsFavoritesUseCase;
import br.com.luizfp.segredosufsc.segredo.GetSecretsUseCase;
import br.com.luizfp.segredosufsc.segredo.UpdateFavoritesUseCase;
import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.event.SecretAddedToFavoritesEvent;
import br.com.luizfp.segredosufsc.event.SecretRemovedFromFavoritesEvent;
import br.com.luizfp.segredosufsc.event.SwipeToFirstTabEvent;
import br.com.luizfp.segredosufsc.exceptions.DefaultErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorMessageFactory;
import br.com.luizfp.segredosufsc.mvp.MvpPresenter;
import br.com.luizfp.segredosufsc.comentario.ComentarioViewModel;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.RxUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luiz on 3/15/16.
 */
@SuppressWarnings("ConstantConditions")
public class SegredosFragmentPresenterImpl extends MvpPresenter<SegredosFragmentView>
        implements SegredosFragmentPresenter {

    private static final String TAG = SegredosFragmentPresenterImpl.class.getSimpleName();
    private String EVENT_NOTIFY_REDRAW_UI = "notify_ui";
    private UseCase mGetSecretsUseCase;
    private UseCase mGetSecretsFavoritesUseCase;
    private UseCase mUpdateFavoritesUseCase;
    private List<SegredoViewModel> mSegredosList;
    private boolean mPagination;
    private String mTipo;

    public SegredosFragmentPresenterImpl(SegredosFragmentView segredosFragmentView) {
        attachView(segredosFragmentView);
    }

    @Override
    public void onUiCreated(boolean isFirstCreation) {
        if (isViewAttached() && isFirstCreation) {
            L.d(TAG, "Carregando segredos pela primeira vez");
            getView().loadingNewItems();
            getView().showLoading();

            if (mTipo != null) {
                if (mTipo.equals(SegredosFragment.TIPO_TODOS)) {
                    mGetSecretsUseCase = new GetSecretsUseCase(
                            Schedulers.io(),
                            AndroidSchedulers.mainThread())
                            .init(getView().context(), SegredosFragment.LIMIT, 0);
                    mGetSecretsUseCase.execute(new GetSecretsSubscriber());
                } else {
                    mGetSecretsFavoritesUseCase = new GetSecretsFavoritesUseCase(
                            Schedulers.io(),
                            AndroidSchedulers.mainThread())
                            .init(getView().context(), SegredosFragment.LIMIT, 0);
                    mGetSecretsFavoritesUseCase.execute(new GetSecretsFavoritesSubscriber());
                }
            }

        } else if(isViewAttached() && !isFirstCreation) {
            if (mSegredosList != null)
                showData(mSegredosList);
        }
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
    public void onNeedToSwipeToFirstTab() {
        EventBus.getDefault().post(new SwipeToFirstTabEvent());
    }

    @Override
    public void updateNumberOfComments(Long idSegredo, List<ComentarioViewModel> newCommentsList) {
        if (mSegredosList != null) {
            for (SegredoViewModel segredo : mSegredosList) {
                if (segredo.getId().equals(idSegredo)) {
                    segredo.setComentarioList(newCommentsList);
                    if (isViewAttached())
                        getView().redrawList();
                    return;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        detachView();
        RxUtils.unsubscribeIfNotNull(mGetSecretsUseCase);
        RxUtils.unsubscribeIfNotNull(mGetSecretsFavoritesUseCase);
        RxUtils.unsubscribeIfNotNull(mUpdateFavoritesUseCase);
    }

    @Override
    public void onClickToFavorite(int position, boolean addToFavorite) {
        SegredoViewModel segredo = mSegredosList.get(position);
        createFavoriteUseCase(segredo.getId(), addToFavorite);
        // Único jeito de adicionar algo aos favoritos é se o usuário está na aba de "TODOS".
        // Sendo assim, não precisa verificar qual o tipo atual, apenas verificar o addToFavorite.
        if (addToFavorite) {
            segredo.setFavorite(true);
            segredo.setTotalFavorites(segredo.getTotalFavorites() + 1);
            if (isViewAttached()) {
                getView().redrawList(); // Para refletir o +1 nos favorites
            }
            SecretAddedToFavoritesEvent event = new SecretAddedToFavoritesEvent();
            event.setSegredo(segredo);
            EventBus.getDefault().post(event);
        } else {
            // Já para remover dos favoritos, pode ser em qualquer uma das duas abas, sendo assim,
            // é preciso verificar o tipo.
            // Comos os dois presenters serão chamados, o segredo já terá o favorito setado para
            // false nos dois.
            segredo.setFavorite(false);
            if (segredo.getTotalFavorites() > 0)
                segredo.setTotalFavorites(segredo.getTotalFavorites() - 1);

            // Agora os específicos
            if (mTipo.equals(SegredosFragment.TIPO_FAVORITOS)) {
                // Se tiver na aba de favoritos, anima a retirada do item da tela e remove da lista
                // Após isso, manda um evento avisando a aba "todos" para ela poder se redesenhar
                // refletindo assim a mudança do "coração" (item não mais favorito).
                mSegredosList.remove(position);
                if (isViewAttached()) {
                    getView().removeSecretFromFavoritesTab(position);
                }
                EventBus.getDefault().post(new SecretRemovedFromFavoritesEvent.NotifyDataSedChanged());
            } else {
                if (isViewAttached())
                    getView().redrawList();
                // Se estiver na aba todos, avisa a aba favoritos para poder remover o item daquela
                // aba
                SecretRemovedFromFavoritesEvent.RemoveItemFromList event =
                        new SecretRemovedFromFavoritesEvent.RemoveItemFromList();
                event.setIdSegredo(segredo.getId());
                EventBus.getDefault().post(event);
            }
        }
    }

    private void createFavoriteUseCase(Long idSegredo, boolean addToFavorite) {
        if (mUpdateFavoritesUseCase == null) {
            mUpdateFavoritesUseCase = new UpdateFavoritesUseCase(Schedulers.io(),
                    AndroidSchedulers.mainThread());
        }

        if (isViewAttached()) {
            ((UpdateFavoritesUseCase)mUpdateFavoritesUseCase).init(getView().context(), idSegredo,
                    addToFavorite);
            mUpdateFavoritesUseCase.execute(new UpdateFavoritesSubscriber(addToFavorite));
        }
    }

    @Subscribe
    public void onNeedToRemoveItemFromFavorites(SecretRemovedFromFavoritesEvent.RemoveItemFromList event) {
        if (mTipo.equals(SegredosFragment.TIPO_FAVORITOS)) {
            L.d(TAG, "Evento para remover item da lista de favoritos chamado");
            for (Iterator<SegredoViewModel> iterator = mSegredosList.iterator(); iterator.hasNext();) {
                SegredoViewModel s = iterator.next();
                if (s.getId().equals(event.getIdSegredo())) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();
                    if (isViewAttached())
                        getView().redrawList();
                    return;
                }
            }
        }
    }

    @Subscribe
    public void onNeedToRedrawListTodos(SecretRemovedFromFavoritesEvent.NotifyDataSedChanged event) {
        if (mTipo.equals(SegredosFragment.TIPO_TODOS)) {
            L.d(TAG, "Evento para redesenhar a lista chamado");
            if (isViewAttached())
                getView().redrawList();
        }
    }

    @Subscribe
    public void onSecretAddedToFavorites(SecretAddedToFavoritesEvent event) {
        if (mTipo.equals(SegredosFragment.TIPO_FAVORITOS)) {
            L.d(TAG, "Evento para adicionar item na lista de favoritos chamado");
            L.d(TAG, "Tamanho lista de segredos: " + mSegredosList.size());
            mSegredosList.add(0, event.getSegredo());
            // Adiciona sempre no início, pois mesmo podendo ficar fora da ordem de data
            // ficará melhor para o usuário localizar os recém adicionados por ele
            if (isViewAttached())
                getView().addSecretToFavoritesTab(0);
        }
    }

    @Override
    public void onNewDataNeeded(long offset, boolean pagination) {
        L.d(TAG, "Carregando novos dados");
        L.d(TAG, "Tipo do loading: " + mTipo);
        this.mPagination = pagination;
        if (mTipo.equals(SegredosFragment.TIPO_TODOS) && mGetSecretsUseCase != null) {
            if (isViewAttached()) {
                prepareNewLoading();
                ((GetSecretsUseCase)
                        mGetSecretsUseCase)
                        .init(getView().context(), SegredosFragment.LIMIT, offset)
                        .execute(new GetSecretsSubscriber());
            }
        } else if (mTipo.equals(SegredosFragment.TIPO_FAVORITOS) && mGetSecretsFavoritesUseCase != null) {
            if (isViewAttached()) {
                prepareNewLoading();
                ((GetSecretsFavoritesUseCase) mGetSecretsFavoritesUseCase)
                        .init(getView().context(), SegredosFragment.LIMIT, offset)
                        .execute(new GetSecretsFavoritesSubscriber());
            }
        }
    }

    private void prepareNewLoading() {
        getView().loadingNewItems();
        if (mPagination)
            getView().showPaginationLoading();
    }

    @Override
    public SegredoViewModel getSegredo(int position) {
        return mSegredosList.get(position);
    }

    @Override
    public String getTipo() {
        return mTipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.mTipo = tipo;
    }

    private void dataReceived(List<SegredoViewModel> segredosList) {
        if (mPagination)
            this.mSegredosList.addAll(segredosList);
        else
            this.mSegredosList = segredosList;
    }

    private void showData(List<SegredoViewModel> segredosList) {
        if (isViewAttached()) {
            getView().showData(segredosList);
        }
    }

    private void hideLoading() {
        if (isViewAttached())
            getView().hideLoading();
    }

    private void showError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView().showError(ErrorMessageFactory.create(getView().context(), errorBundle.getException()));
        }
    }

    private void favoriteAddedRemoveWithSuccess(boolean addToFavorite) {
        if (isViewAttached()) {
            if (addToFavorite)
                getView().showMessage("Adicionado com sucesso", true);
            else
                getView().showMessage("Removido com sucesso", true);
        }
    }

    private class UpdateFavoritesSubscriber extends DefaultSubscriber<Response> {
        private final boolean mAddToFavorite;

        private UpdateFavoritesSubscriber(boolean addToFavorite) {
            this.mAddToFavorite = addToFavorite;
        }

        @Override
        public void onNext(Response response) {
            super.onNext(response);
            if (response != null && response.getStatus().equals(Response.OK)) {
                L.d(TAG, response.getMsg());
                SegredosFragmentPresenterImpl.this.favoriteAddedRemoveWithSuccess(mAddToFavorite);
            } else {
                Log.e(TAG, "Erro ao vincular/desvincular segredo dos favoritos do usuário");
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao vincular/desvincular segredo dos favoritos do usuário", e);
            SegredosFragmentPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }

    private class GetSecretsSubscriber extends DefaultSubscriber<List<SegredoViewModel>> {

        @Override
        public void onNext(List<SegredoViewModel> segredos) {
            super.onNext(segredos);
            SegredosFragmentPresenterImpl.this.hideLoading();
            L.d(TAG, "Lista de segredos recebida: " + segredos);
            if (segredos != null) {
                L.d(TAG, "Lista de segredos recebida com sucesso");
                L.d(TAG, "Size lista recebida: " + segredos.size());
                SegredosFragmentPresenterImpl.this.dataReceived(segredos);
                SegredosFragmentPresenterImpl.this.showData(segredos);
            } else {
                Log.e(TAG, "Lista de segredos veio nula ou vazia");
                SegredosFragmentPresenterImpl.this.showError(new DefaultErrorBundle(new Exception()));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao buscar segredos", e);
            SegredosFragmentPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }

    private class GetSecretsFavoritesSubscriber extends DefaultSubscriber<List<SegredoViewModel>> {

        @Override
        public void onNext(List<SegredoViewModel> segredos) {
            super.onNext(segredos);
            SegredosFragmentPresenterImpl.this.hideLoading();
            L.d(TAG, "Lista de segredos recebida: " + segredos);
            if (segredos != null) {
                L.d(TAG, "Lista de segredos recebida com sucesso");
                L.d(TAG, "Size lista recebida: " + segredos.size());
                SegredosFragmentPresenterImpl.this.dataReceived(segredos);
                SegredosFragmentPresenterImpl.this.showData(segredos);
            } else {
                Log.e(TAG, "Lista de segredos veio nula ou vazia");
                SegredosFragmentPresenterImpl.this.showError(new DefaultErrorBundle(new Exception()));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao buscar segredos", e);
            SegredosFragmentPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }
}