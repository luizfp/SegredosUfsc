package br.com.luizfp.segredosufsc.new_implementation.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import br.com.luizfp.segredosufsc.new_implementation.internal.di.HasComponent;
import butterknife.Unbinder;

/**
 * Created by luiz on 2/23/16.
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    protected Unbinder mUnbinder;

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

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
}