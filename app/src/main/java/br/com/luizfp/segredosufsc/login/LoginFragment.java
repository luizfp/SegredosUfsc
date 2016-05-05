package br.com.luizfp.segredosufsc.login;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.luizfp.segredosufsc.mvp.MvpFragment;
import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.base.BaseAnimatorListener;
import br.com.luizfp.segredosufsc.login.confirm_access_code.ConfirmAccessCodeActivity;
import br.com.luizfp.segredosufsc.login.confirm_access_code.ConfirmAccessCodeFragment;
import br.com.luizfp.segredosufsc.ui.LoadingButton;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.TypefaceHelper;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends MvpFragment<LoginPresenter> implements LoginView {
    private static final String TAG = LoginFragment.class.getSimpleName();
    @Bind(R.id.txt_fraseLogin) TextView mTxtFraseLogin;
    @Bind(R.id.txt_appName) TextView mTxtAppName;
    @Bind(R.id.img_login) ImageView mImgLogin;
    @Bind(R.id.layout_textLogin) ViewGroup mLayoutTextLogin;
    @Bind(R.id.edt_email) EditText mEdtEmail;
    @Bind(R.id.btn_login) LoadingButton mBtnLogin;
    @BindDimen(R.dimen.fixed_translationy_value_on_rotation) float mBestShotDimen;

    public LoginFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        Typeface typefaceFrase = TypefaceHelper.get(getContext(), "josefin_sans_light_italic.ttf");
        Typeface typefaceAppName = TypefaceHelper.get(getContext(), "ubuntu_light.ttf");
        mTxtFraseLogin.setTypeface(typefaceFrase);
        mTxtAppName.setTypeface(typefaceAppName);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected LoginPresenter instantiatePresenter() {
        return new LoginPresenterImpl(this);
    }

    @OnClick(R.id.btn_login)
    public void onClickToLogin(View view) {
        String email = mEdtEmail.getText().toString().trim().toLowerCase();
        mPresenter.verifyEmail(email);
    }

    @Override
    public void showNotAnEmailUfscError(String errorMessage) {
        mEdtEmail.setError(errorMessage);
    }

    @Override
    public void showLoading() {
        mBtnLogin.showLoading();
    }

    @Override
    public void hideLoading() {
        mBtnLogin.stopLoading();
    }

    @Override
    public void showError(String errorMessage) {
        snack(mEdtEmail, errorMessage);
    }

    @Override
    public void navigateToConfirmAccessCode(Long idCodigoAcesso, char supostaInicialNome) {
        Intent intent = new Intent(getActivity(), ConfirmAccessCodeActivity.class);
        intent.putExtra(ConfirmAccessCodeFragment.EXTRA_ID_CODIGO_ACESSO, idCodigoAcesso);
        intent.putExtra(ConfirmAccessCodeFragment.EXTRA_SUPOSTA_INICIAL_USUARIO, supostaInicialNome);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startLogoAnimation() {
        mImgLogin
                .animate()
                .alpha(0f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(1500)
                .setStartDelay(600)
                .setListener(new BaseAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        float y = (mImgLogin.getHeight() / 2) + (mLayoutTextLogin.getHeight() / 2);
                        L.d(TAG, "Layout deve subir: " + y);
                        mImgLogin.setVisibility(View.INVISIBLE);
                        mLayoutTextLogin.animate().translationY(-y).setDuration(1000).start();
                    }
                })
                .start();
    }

    @Override
    public void changeAnimationToFinalState() {
        /**
         * Aqui a tela ainda não está criada então não podemos recuperar o tamanho correto
         * que a view deve subir. No final da animação é possível armazenar o valor de y localmente
         * e depois usá-lo aqui, mas o usuário pode rotacionar a tela antes a animação finalizar
         * sendo assim y ainda seria 0. Para corrigir isso, foi fixado uma altura que a view deve
         * subir quando não é a primeira criação da tela. No caso, 50dp.
         */
        mImgLogin.setVisibility(View.INVISIBLE);
        mLayoutTextLogin.animate().translationY(-mBestShotDimen).setDuration(0).start();
    }

    @Override
    public void emailSendWithSuccess() {
        mBtnLogin.showSuccess("ENVIADO! :D");
    }

    @Override
    public Context context() {
        return getContext().getApplicationContext();
    }

}
