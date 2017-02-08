package br.com.luizfp.segredosufsc.login.confirm_access_code;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.Arrays;
import java.util.List;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.Curso;
import br.com.luizfp.segredosufsc.mvp.MvpFragment;
import br.com.luizfp.segredosufsc.segredo.activity.SegredosActivity;
import br.com.luizfp.segredosufsc.util.AlfabetoHelper;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.TypefaceHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.luizfp.segredosufsc.R.array.alfabeto;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmAccessCodeFragment extends  MvpFragment<ConfirmAccessCodePresenter> implements
        ConfirmAccessCodeView, WheelPicker.OnWheelChangeListener {

    public static final String EXTRA_ID_CODIGO_ACESSO = "extra_id_codigo_acesso";

    /**
     * Assume que a primeira letra do email colocado na tela anterior é inicial do nome usuário
     */
    public static final String EXTRA_SUPOSTA_INICIAL_USUARIO = "extra_suposta_inicial_usuario";
    private static final String TAG = ConfirmAccessCodeActivity.class.getSimpleName();
    @BindView(R.id.edt_codigoAcesso) EditText mEdtCodigoAcesso;
    @BindView(R.id.wheelPicker) WheelPicker mWheelPicker;
    @BindView(R.id.fab_confirmarCodigo) FloatingActionButton mFabConfirmarCodigo;
    @BindView(R.id.progress_confirmAccessCode) ProgressBar mProgressBar;
    @BindView(R.id.spn_cursosUfsc) Spinner mSpnCursosUfsc;
    @BindView(R.id.txt_avisoCodigoAcesso) TextView mTxtAvisoCodigoAcesso;
    @BindView(R.id.txt_qualSeuCurso) TextView mtxtQualSeuCurso;
    @BindView(R.id.txt_qualSuaInicial) TextView mTxtQualSuaInicial;
    @BindView(R.id.img_confirmSuccess) ImageView mImgSuccess;
    @BindView(R.id.layout_loadingCoursesList) ViewGroup layoutLoadingCourses;
    private String[] mAlfabeto;

    public ConfirmAccessCodeFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoveryIdCodigoAcessoAndInicial();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_access_code, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mEdtCodigoAcesso.setFilters(
                new InputFilter[]{
                        new InputFilter.AllCaps(),
                        new InputFilter.LengthFilter(getResources().getInteger(R.integer.confirm_access_code_length))});
        setTypefaces();
        return view;
    }

    private void setTypefaces() {
        Typeface typefaceUbuntuLight = TypefaceHelper.get(getContext(), "ubuntu_light.ttf");
        mTxtAvisoCodigoAcesso.setTypeface(typefaceUbuntuLight);
        mtxtQualSeuCurso.setTypeface(typefaceUbuntuLight);
        mTxtQualSuaInicial.setTypeface(typefaceUbuntuLight);
    }

    private void recoveryIdCodigoAcessoAndInicial() {
        Bundle bundle = getArguments();
        if (bundle != null
                && bundle.containsKey(EXTRA_ID_CODIGO_ACESSO)
                && bundle.containsKey(EXTRA_SUPOSTA_INICIAL_USUARIO)) {
            mPresenter.setIdCodigoAcesso(bundle.getLong(EXTRA_ID_CODIGO_ACESSO));
            mPresenter.setSelectedLetter(bundle.getChar(EXTRA_SUPOSTA_INICIAL_USUARIO));
        }
    }

    @Override
    public void fillLettersForSelection(char supostaInicialNome) {
        mAlfabeto = getResources().getStringArray(alfabeto);
        L.d(TAG, "Length alfabeto: " + mAlfabeto.length);
        mWheelPicker.setData(Arrays.asList(alfabeto));
        mWheelPicker.setItemTextSize(100);
        mWheelPicker.setItemTextColor(ContextCompat.getColor(getContext(), R.color.login_button));
        mWheelPicker.setSelectedItemTextColor(ContextCompat.getColor(getContext(), R.color.white));
//        mWheelPicker.setOrientation(WheelCrossPicker.HORIZONTAL);
        mWheelPicker.setSelectedItemPosition(
                AlfabetoHelper.getIndexForLetter(mAlfabeto, String.valueOf(supostaInicialNome)));
        mWheelPicker.setOnWheelChangeListener(this);
    }

    @OnClick(R.id.fab_confirmarCodigo)
    public void onClickFabConfirmarCodigo(View view) {
        String code = mEdtCodigoAcesso.getText().toString().trim().toUpperCase();
        Curso curso = (Curso) mSpnCursosUfsc.getSelectedItem();
        if (curso != null)
            mPresenter.verifyCode(code, curso);
    }

    @Override
    protected ConfirmAccessCodePresenter instantiatePresenter() {
        return new ConfirmAccessCodePresenterImpl(this);
    }

    @Override
    public Context context() {
        return getContext().getApplicationContext();
    }

    @Override
    public void showLoadingCourses() {
        mSpnCursosUfsc.setVisibility(View.GONE);
        layoutLoadingCourses.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingCourses() {
        layoutLoadingCourses.setVisibility(View.GONE);
        mSpnCursosUfsc.setVisibility(View.VISIBLE);
    }

    @Override
    public void fiilCoursesForSelection(List<Curso> coursesList) {
        //String[] cursosUfsc = getResources().getStringArray(R.array.cursos_ufsc);
        L.d(TAG, "Length Cursos ufsc: " + coursesList.size());
        ArrayAdapter<Curso> cursosArray= new ArrayAdapter<>(getContext(),
                R.layout.partial_spinner_item, coursesList);
        cursosArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnCursosUfsc.setAdapter(cursosArray);
    }

    @Override
    public void navigateToSegredos(char letterSelected) {
        Intent intent = new Intent(getActivity(), SegredosActivity.class);
        intent.putExtra(SegredosActivity.EXTRA_LETTER_SELECTED, letterSelected);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mProgressBar.setVisibility(View.INVISIBLE);
        mImgSuccess.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            startActivity(intent);
            getActivity().finish();
        }, 1000);
    }

    @OnClick(R.id.imgBtn_infoInicial)
    public void onClickToShowInfosAboutInicial(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_aviso_inicial, null);
        builder.setView(v);
        builder.show();
    }

    @Override
    public void showLoading() {
        mSpnCursosUfsc.setEnabled(false);
        mEdtCodigoAcesso.setFocusable(false);
        mFabConfirmarCodigo.hide();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mSpnCursosUfsc.setEnabled(true);
        mEdtCodigoAcesso.setFocusableInTouchMode(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mFabConfirmarCodigo.show();
    }

    @Override
    public void showError(String errorMessage) {
        toast(errorMessage);
    }

    @Override
    public void onWheelScrolled(int offset) {
        // Empty
    }

    @Override
    public void onWheelSelected(int position) {
        mPresenter.setSelectedLetter(mAlfabeto[position].charAt(0));
    }

    @Override
    public void onWheelScrollStateChanged(int newState) {
        if (newState != WheelPicker.SCROLL_STATE_IDLE) {
            mFabConfirmarCodigo.setEnabled(false);
        } else {
            mFabConfirmarCodigo.setEnabled(true);
        }
    }
}
