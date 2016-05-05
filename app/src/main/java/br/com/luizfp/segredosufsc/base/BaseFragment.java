package br.com.luizfp.segredosufsc.base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import br.com.luizfp.segredosufsc.Application;

/**
 * Created by luiz on 2/23/16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        Application.getRefWatcher(getActivity()).watch(this);
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void snack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    /**
     * @return Context
     */
    public Context getContext() {
        return getActivity();
    }
}
