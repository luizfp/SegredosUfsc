package br.com.luizfp.segredosufsc.ui.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCrossPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;

import java.util.Arrays;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.util.AlfabetoHelper;
import br.com.luizfp.segredosufsc.util.L;
import butterknife.Bind;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by luiz on 2/27/16.
 */
public final class SelectLetterDialog extends PersistentDialog implements AbstractWheelPicker.OnWheelChangeListener {
    private static final String TAG = SelectLetterDialog.class.getSimpleName();
    public static final String DIALOG_TAG = "select_inicial_dialog";
    public static final String LETRA_ATUAL = "letra_atual";
    @BindView(R.id.wheelPicker) WheelCurvedPicker wheelCurvedPicker;
    @BindView(R.id.btn_select) Button btnSelect;
    @BindView(R.id.layout_button_select) ViewGroup layoutButtonSelect;
    private OnSelectLetra callback;
    private String newLetraSelected;
    private String letraAtual;
    private int indexAtual = 0;

    public interface OnSelectLetra {
        void onNewLetraSelected(char letra);
        void onSameLetraSelected();
    }

    public static SelectLetterDialog newInstance() {
        return new SelectLetterDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnSelectLetra) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getClass().getSimpleName() + " must " +
                    "implement OnSelectLetra");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().setTitle("A sua inicial. Ou não.");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_inicial, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(LETRA_ATUAL)) {
            letraAtual = bundle.getString(LETRA_ATUAL);
            L.d(TAG, "Letra atual recebida: " + letraAtual);
        }
        initWheelPicker();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    private void initWheelPicker() {
        String[] alfabeto = getResources().getStringArray(R.array.alfabeto);
        L.d(TAG, "Length alfabeto: " + alfabeto.length);
        wheelCurvedPicker.setData(Arrays.asList(alfabeto));
        wheelCurvedPicker.setTextSize(70);
        wheelCurvedPicker.setCurrentTextColor(ContextCompat.getColor(getContext(), R.color.login_background));
        wheelCurvedPicker.setOrientation(WheelCrossPicker.HORIZONTAL);
        wheelCurvedPicker.setOnWheelChangeListener(this);
        indexAtual = AlfabetoHelper.getIndexForLetter(alfabeto, letraAtual);
        L.d(TAG, "initWheelPicker() index:" + indexAtual);
        wheelCurvedPicker.setItemIndex(indexAtual);
    }


    @OnClick(R.id.btn_select)
    public void onClickToSelect(View view) {
        L.d(TAG, "onClickToSelect() letraAtual: " + letraAtual);
        L.d(TAG, "onClickToSelect() newLetraSelected: " + newLetraSelected);
        L.d(TAG, "onClickToSelect() callback: " + callback);
        if (letraAtual != null && newLetraSelected != null) {
            if (letraAtual.equals(newLetraSelected)) {
                if (callback != null) {
                    callback.onSameLetraSelected();
                    dismiss();
                }
            } else {
                if (callback != null) {
                    callback.onNewLetraSelected(newLetraSelected.charAt(0));
                    dismiss();
                }
            }
        }
    }

    @Override
    public void onWheelScrolling(float deltaX, float deltaY) {

    }

    @Override
    public void onWheelSelected(int index, String data) {
        L.d(TAG, "onWheelSelected() index:" + index);
        indexAtual = index;
        newLetraSelected = data;
    }

    @Override
    public void onWheelScrollStateChanged(int newState) {
        if (newState != AbstractWheelPicker.SCROLL_STATE_IDLE) {
            enableButton(false);
        } else {
            enableButton(true);
        }
    }

    private void enableButton(boolean enable) {
        if (enable) {
            btnSelect.setEnabled(true);
            layoutButtonSelect.setBackgroundResource(R.drawable.partial_rounded_button_dialog_inicial_enable);
        } else {
            btnSelect.setEnabled(false);
            layoutButtonSelect.setBackgroundResource(R.drawable.partial_rounded_button_dialog_inicial_disable);
        }
    }
}
