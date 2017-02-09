package br.com.luizfp.segredosufsc.new_implementation.segredo.novo;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import br.com.luizfp.segredosufsc.OnSendListener;
import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.base.BaseFragment;
import br.com.luizfp.segredosufsc.new_implementation.Fonts;
import br.com.luizfp.segredosufsc.ui.fragments.dialog.ConfirmarEnvioDialog;
import br.com.luizfp.segredosufsc.util.TypefaceHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovoSegredoFragment extends BaseFragment implements
        OnSendListener,
        NovoSegredoContract.View {
    @BindView(R.id.txt_anonimo) TextView mTxtAnonimo;
    @BindView(R.id.edt_novoSegredo) EditText mEdtNovoSegredo;

    public NovoSegredoFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_novo_segredo, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        setTypeface();
        return view;
    }

    @Override
    public void onSend() {
        toast("Precisamos enviar ainda!!!");
    }

    @Override
    public void onCancel() {
        toast("Segredo não enviado :/");
    }


    @Override
    public Context context() {
        return getContext().getApplicationContext();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick(R.id.fab_novoSegredo)
    public void onClickFab(View view) {
        showDialog(getFragmentManager());
    }

    private void setTypeface() {
        Typeface typefaceAnonimo = TypefaceHelper.get(getContext(), Fonts.UBUNTU_LIGHT);
        mTxtAnonimo.setTypeface(typefaceAnonimo);
    }

    private void showDialog(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(ConfirmarEnvioDialog.DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFrag = ConfirmarEnvioDialog.newInstance();
        dialogFrag.setTargetFragment(this, 0);
        dialogFrag.show(ft, ConfirmarEnvioDialog.DIALOG_TAG);
    }
}