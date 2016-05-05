package br.com.luizfp.segredosufsc.segredo.novo;


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
import br.com.luizfp.segredosufsc.ui.fragments.dialog.ConfirmarEnvioDialog;
import br.com.luizfp.segredosufsc.util.TypefaceHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovoSegredoFragment extends BaseFragment
        implements OnSendListener {
    public static final String FRAG_TAG = "novo_segredo_fragment";
    @Bind(R.id.txt_anonimo) TextView mTxtAnonimo;
    @Bind(R.id.edt_novoSegredo) EditText mEdtNovoSegredo;
    private OnClickToSend mCallback;


    public interface OnClickToSend {
        void onSend(String segredo);
    }

    public NovoSegredoFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnClickToSend) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getClass().getSimpleName() +
                    "must implement OnClickToSend");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_novo_segredo, container, false);
        ButterKnife.bind(this, view);
        setTypeface();
        return view;
    }

    private void setTypeface() {
        Typeface typefaceAnonimo = TypefaceHelper.get(getContext(), "ubuntu_light.ttf");
        mTxtAnonimo.setTypeface(typefaceAnonimo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSend() {
        if (mCallback != null) {
            mCallback.onSend(mEdtNovoSegredo.getText().toString().trim());
        }
    }

    @Override
    public void onCancel() {
        toast("Segredo não enviado :/");
    }

    @OnClick(R.id.fab_novoSegredo)
    public void onClickFab(View view) {
        showDialog(getFragmentManager());
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
