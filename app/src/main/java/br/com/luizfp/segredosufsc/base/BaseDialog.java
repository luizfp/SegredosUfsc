package br.com.luizfp.segredosufsc.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.luizfp.segredosufsc.util.L;

/**
 * Created by luiz on 2/25/16.
 */
public abstract class BaseDialog extends DialogFragment {
    private static final String TAG = BaseDialog.class.getSimpleName();

    // =========================================
    //
    // LIFECYCLE
    //
    // =========================================
    @Override
    public void onAttach(Context context) {
        L.d(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        L.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.d(TAG, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        L.d(TAG, "onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        L.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        L.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }
}
