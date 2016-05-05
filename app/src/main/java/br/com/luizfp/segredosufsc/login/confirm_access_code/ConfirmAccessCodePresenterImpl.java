package br.com.luizfp.segredosufsc.login.confirm_access_code;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.luizfp.segredosufsc.mvp.MvpPresenter;
import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.login.ConfirmAccessCodeUseCase;
import br.com.luizfp.segredosufsc.domain.DefaultSubscriber;
import br.com.luizfp.segredosufsc.login.GetCoursesUseCase;
import br.com.luizfp.segredosufsc.domain.UseCase;
import br.com.luizfp.segredosufsc.exceptions.DefaultErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.DefaultMessageException;
import br.com.luizfp.segredosufsc.exceptions.ErrorBundle;
import br.com.luizfp.segredosufsc.exceptions.ErrorMessageFactory;
import br.com.luizfp.segredosufsc.network.Credentials;
import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.Prefs;
import br.com.luizfp.segredosufsc.util.RxUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by luiz on 3/9/16.
 */
@SuppressWarnings("ConstantConditions")
public class ConfirmAccessCodePresenterImpl extends MvpPresenter<ConfirmAccessCodeView> implements
    ConfirmAccessCodePresenter {

    private static final String TAG = ConfirmAccessCodePresenterImpl.class.getSimpleName();
    /**
     * O código de acesso de 4 digitos gerado é salvo juntamente com um ID no banco de dados.
     * E como pode acontecer de ser gerado a mesma sequência para duas pessoas diferentes, cada
     * código possui também um ID que é ÚNICO. Assim, na hora de confirmar o código de acesso ao
     * app recebido pelo email, é necessário enviar esse ID junto na requisição.
     */
    private Long mIdCodigoAcesso;

    private UseCase mConfirAccessCodeUseCase;
    private UseCase mGetCoursesUseCase;
    private char mLetterSelected;
    private List<Curso> mCoursesList;
    private Curso mCourseSelected;

    public ConfirmAccessCodePresenterImpl(ConfirmAccessCodeView confirmAccessCodeView) {
        attachView(confirmAccessCodeView);
    }

    @Override
    public void onUiCreated(boolean isFirstCreation) {
        setIsFirstUiCreation(isFirstCreation);
        if (isViewAttached()) {
            getView().fillLettersForSelection(mLetterSelected);
        }
    }

    @Override
    public void onResume() {
        if (isViewAttached()) {
            if (isFirstUiCreation()) {
                mGetCoursesUseCase = new GetCoursesUseCase(Schedulers.io(),
                        AndroidSchedulers.mainThread(), getView().context());
                mGetCoursesUseCase.execute(new GetCoursesSubscriber());
            }
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        L.d(TAG, "onDestroy()");
        detachView();
        RxUtils.unsubscribeIfNotNull(mConfirAccessCodeUseCase);
        RxUtils.unsubscribeIfNotNull(mGetCoursesUseCase);
    }

    @Override
    public void verifyCode(String code, Curso curso) {
        this.mCourseSelected = curso;
        if (code.length() == 4) {
            if (isViewAttached()) {
                getView().showLoading();
                mConfirAccessCodeUseCase = new ConfirmAccessCodeUseCase(Schedulers.io(),
                        AndroidSchedulers.mainThread(), getView().context(), mIdCodigoAcesso,
                        code, mLetterSelected, curso.getId());
                mConfirAccessCodeUseCase.execute(new ConfirmAccessCodeSubscriber());
            }
        } else {
            showError(new DefaultErrorBundle(new DefaultMessageException(getText(R.string.error_message_wrong_length_access_code))));
        }
    }

    @Override
    public void setIdCodigoAcesso(Long idCodigoAcesso) {
        this.mIdCodigoAcesso = idCodigoAcesso;
    }

    @Override
    public void setSelectedLetter(char newLetter) {
        L.d(TAG, "Nova letra selecionada: " + newLetter);
        this.mLetterSelected = newLetter;
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showError(ErrorBundle errorBundle) {
        if (isViewAttached()) {
            getView().showError(ErrorMessageFactory.create(getView().context(), errorBundle.getException()));
        }
    }

    private void confirmationSuccess() {
        if (isViewAttached()) {
            getView().navigateToSegredos(mLetterSelected);
        }
    }

    private String getText(int resId) {
        if (isViewAttached()) {
            return getView().context().getString(resId);
        }
        return "Erro";
    }

    private void coursesRetrieved(List<Curso> coursesList) {
        this.mCoursesList = coursesList;
        if (isViewAttached()) {
            getView().hideLoadingCourses();
            getView().fiilCoursesForSelection(coursesList);
        }
    }

    // TODO: Se colocar o código errado, não vai aparecer um erro dizendo que foi esse o problema
    private class ConfirmAccessCodeSubscriber extends DefaultSubscriber<Credentials> {

        @Override
        public void onNext(Credentials credentials) {
            super.onNext(credentials);
            if (credentials != null && credentials.getIdUser() != -1L) {
                L.d(TAG, "Thread name: " + Thread.currentThread().getName());
                if (isViewAttached()) {
                    Context context = getView().context().getApplicationContext();
                    // Não há problemas em salvar na SharedPreferences em outra thread.
                    // A implementação da SharedPreferences no android é thread safe.
                    Observable
                            .create(subscriber -> {
                                L.d(TAG, "Thread name: " + Thread.currentThread().getName());
                                Prefs.putString(context, Prefs.TOKEN_KEY, credentials.getAccessToken());
                                Prefs.putBoolean(context, Prefs.ESTA_LOGADO_KEY, true);
                                Prefs.putLong(context, Prefs.ID_USER_KEY, credentials.getIdUser());
                                Prefs.putChar(context, Prefs.INICIAL_USER_KEY, mLetterSelected);
                                Prefs.putString(context, Prefs.COURSE_USER_KEY, mCourseSelected.getNome());
                            })
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
                ConfirmAccessCodePresenterImpl.this.confirmationSuccess();
            } else {
                Log.e(TAG, "Erro. Credenciais vieram nulas");
                ConfirmAccessCodePresenterImpl.this.hideLoading();
                ConfirmAccessCodePresenterImpl.this.showError(new DefaultErrorBundle(
                        new Exception()));
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao confirmar código de acesso", e);
            ConfirmAccessCodePresenterImpl.this.hideLoading();
            ConfirmAccessCodePresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }

    private class GetCoursesSubscriber extends DefaultSubscriber<List<Curso>> {

        @Override
        public void onNext(List<Curso> cursos) {
            super.onNext(cursos);
            if (cursos != null && cursos.size() > 0) {
                ConfirmAccessCodePresenterImpl.this.coursesRetrieved(cursos);
            } else {
                // TODO: erro
            }
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Erro ao buscar todos os cursos", e);
            ConfirmAccessCodePresenterImpl.this.hideLoading();
            ConfirmAccessCodePresenterImpl.this.showError(new DefaultErrorBundle((Exception) e));
        }
    }
}
