package br.com.luizfp.segredosufsc.comentario;

import android.util.Log;

import java.util.Date;
import java.util.List;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.domain.DefaultSubscriber;
import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.network.Response;
import br.com.luizfp.segredosufsc.exceptions.DefaultErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorMessageFactory;
import br.com.luizfp.segredosufsc.mvp.MvpPresenter;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.Prefs;
import br.com.luizfp.segredosufsc.util.RxUtils;
import br.com.luizfp.segredosufsc.util.TimeUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luiz on 3/17/16.
 */
@SuppressWarnings("ConstantConditions")
public class ComentariosPresenterImpl extends MvpPresenter<ComentariosView>
        implements ComentariosPresenter {

    private static final String TAG = ComentariosPresenterImpl.class.getSimpleName();

    // TODO: Talvez como um usuário pode enviar vários comentários seguidos, vários useCases
    // são criados, assim, só é feito unsubscrib do ultimo. Verificar! Possível leak.
    private UseCase mSendCommentUseCase;
    private UseCase mGetCommentsUseCase;
    private SegredoViewModel mSegredo;

    /**
     * Quando o presenter receber o segredo pela primeira vez, o tamanho da lista de comentários
     * será setado aqui, assim, quando precisamos verificar se o numero de comentários mudou desde o
     * momento que o usuário abriu a tela de comentários até o momento que ele saiu dela (mudanças
     * essas podendo ser por conta de envio de segredos por parte do usuário ou por
     * pullToRefresh), basta comparar esse valor com o tamanho da lista ao sair.
     */
    private int mTamanhoInicialListaComentario;
    private String mComentario;

    public ComentariosPresenterImpl(ComentariosView comentariosView) {
        attachView(comentariosView);
    }

    @Override
    public void onUiCreated(boolean isFirstCreation) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        detachView();
        RxUtils.unsubscribeIfNotNull(mSendCommentUseCase);
        RxUtils.unsubscribeIfNotNull(mGetCommentsUseCase);
    }

    @Override
    public void sendComment(String comentario) {
        this.mComentario = comentario;
        if (mSegredo != null) {
            L.d(TAG, "sendComment(): " + comentario + " " + mSegredo.getId());
            if (isViewAttached()) {
                getView().showLoading();
                mSendCommentUseCase = new SendCommentUseCase(Schedulers.io(),
                        AndroidSchedulers.mainThread(), getView().context(), comentario, mSegredo.getId());
                mSendCommentUseCase.execute(new SendComentSubscriber());
            }
        }
    }

    @Override
    public void setSegredo(SegredoViewModel segredo) {
        this.mTamanhoInicialListaComentario = segredo.getComentarioList().size();
        this.mSegredo = segredo;
    }

    @Override
    public SegredoViewModel getSegredo() {
        return mSegredo;
    }

    @Override
    public void updateComentariosList() {
        if (isViewAttached() &&  mSegredo != null) {
            // Não precisa chamar o showLoading() pois é automático no swipeToRefresh.
            mGetCommentsUseCase = new GetCommentsUseCase(Schedulers.io(),
                    AndroidSchedulers.mainThread(), getView().context(), mSegredo.getId());
            mGetCommentsUseCase.execute(new GetComentsSubscriber());
        }
    }

    @Override
    public boolean thereIsANewNumberOfComments() {
        return mSegredo.getComentarioList().size() > mTamanhoInicialListaComentario;
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView().showError(ErrorMessageFactory.create(getView().context(), errorBundle.getException()));
            getView().enableToResend();
        }
    }

    private void sendWithSuccess() {
        if (isViewAttached()) {
            ComentarioViewModel comentario = new ComentarioViewModel();
            comentario.setComentario(mComentario);
            comentario.setDataString(TimeUtils.toUserFriendlyTimestamp(new Date(System.currentTimeMillis())));
            comentario.setInicialNomeUsuario(Prefs.getChar(getView().context(), Prefs.INICIAL_USER_KEY));
            comentario.setNomeCursoUsuario(Prefs.getString(getView().context(), Prefs.COURSE_USER_KEY));
            mSegredo.getComentarioList().add(comentario);
            getView().redrawList();
            getView().enableToSendAnotherComment();
            getView().showSendWithSuccess(getView().context().getString(R.string.comentario_enviado));
        }
    }

    private void onNewListComentsReceived(List<ComentarioViewModel> comentarios) {
        mSegredo.setComentarioList(comentarios);
        if (isViewAttached())
            getView().updateComentariosList();
    }

    private class SendComentSubscriber extends DefaultSubscriber<Response> {

        @Override
        public void onNext(Response response) {
            super.onNext(response);
            if (response != null && response.getStatus().equals(Response.OK)) {
                ComentariosPresenterImpl.this.hideLoading();
                ComentariosPresenterImpl.this.sendWithSuccess();
            } else {
                Log.e(TAG, "Erro ao enviar comentário");
                ComentariosPresenterImpl.this.hideLoading();
                ComentariosPresenterImpl.this.showError(new DefaultErrorBundle(new Exception()));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao enviar comentário", e);
            ComentariosPresenterImpl.this.hideLoading();
            ComentariosPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }

    private class GetComentsSubscriber extends DefaultSubscriber<List<ComentarioViewModel>> {

        @Override
        public void onNext(List<ComentarioViewModel> comentarios) {
            super.onNext(comentarios);
            // Se a lista buscada for nula ou menor do que a lista que a gente já possui, então
            // tem algo errado
            if (comentarios != null && (comentarios.size() >= mSegredo.getComentarioList().size())) {
                ComentariosPresenterImpl.this.hideLoading();
                ComentariosPresenterImpl.this.onNewListComentsReceived(comentarios);
            } else {
                Log.e(TAG, "Erro ao atualizar lista de comentários");
                ComentariosPresenterImpl.this.hideLoading();
                ComentariosPresenterImpl.this.showError(new DefaultErrorBundle(new Exception()));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao atualizar lista de comentários", e);
            ComentariosPresenterImpl.this.hideLoading();
            ComentariosPresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }
}
