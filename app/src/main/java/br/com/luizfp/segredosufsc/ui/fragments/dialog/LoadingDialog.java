package br.com.luizfp.segredosufsc.ui.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import br.com.luizfp.segredosufsc.R;

/**
 * Created by luiz on 2/25/16.
 */
public final class LoadingDialog extends PersistentDialog {
    public static final String DIALOG_TAG = "loading_dialog";

    public static LoadingDialog newInstance() {
        return new LoadingDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);
        return view;
    }
}
