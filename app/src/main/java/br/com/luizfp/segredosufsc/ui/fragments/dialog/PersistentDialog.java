package br.com.luizfp.segredosufsc.ui.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import br.com.luizfp.segredosufsc.new_implementation.base.BaseDialog;
import br.com.luizfp.segredosufsc.util.L;

/**
 * Created by luiz on 2/25/16.
 */
public abstract class PersistentDialog extends BaseDialog {
    private static final String TAG = PersistentDialog.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "Guardando a instância do dialog");
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        // FIXME: isso corrigi um bug conhecido do android de o dialog ser fechado na troca
        // de orientação se ele estiver com setRetainInstance(true);
        // https://code.google.com/p/android/issues/detail?id=17423
        if (getDialog() != null && getRetainInstance()) {
            L.d(TAG, "Previnindo dialog de dar dismiss na rotação de tela");
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
