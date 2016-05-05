package br.com.luizfp.segredosufsc.ui.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import br.com.luizfp.segredosufsc.OnSendListener;
import br.com.luizfp.segredosufsc.base.BaseDialog;

/**
 * Created by luiz on 12/20/15.
 */
public final class ConfirmarEnvioDialog extends BaseDialog {
    public static final String DIALOG_TAG = "confirmar_envio_dialog_tag";
    private OnSendListener callback;

    public static ConfirmarEnvioDialog newInstance() {
        return new ConfirmarEnvioDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnSendListener) getTargetFragment();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getTargetFragment().getClass().getSimpleName() + " must " +
                    "implement OnSendListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (callback != null) {
                            callback.onSend();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (callback != null) {
                            callback.onCancel();
                        }
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deseja confirmar o envio?");
        builder.setPositiveButton("Sim", dialogClickListener);
        builder.setNegativeButton("Não", dialogClickListener);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (callback != null) {
            callback.onCancel();
        }
    }
}
