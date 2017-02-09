package br.com.luizfp.segredosufsc.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import br.com.luizfp.segredosufsc.new_implementation.base.MvpPresenter;
import br.com.luizfp.segredosufsc.new_implementation.base.MvpView;

/**
 * Created by luiz on 8/4/16.
 */
public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> mWeakView;

    @Override
    public void attachView(@NonNull V view) {
        mWeakView = new WeakReference<>(view);
    }

    /**
     * Get the attached view. You should always call {@link #isViewAttached()} to check if the view
     * is
     * attached to avoid NullPointerExceptions.
     *
     * @return <code>null</code>, if view is not attached, otherwise the concrete view instance
     */
    @Nullable
    public V getView() {
        return mWeakView == null ? null : mWeakView.get();
    }

    /**
     * Checks if a view is attached to this presenter. You should always call this method before
     * calling {@link #getView()} to get the view instance.
     */
    public boolean isViewAttached() {
        return mWeakView != null && mWeakView.get() != null;
    }

    @Override
    public void detachView() {
        if (mWeakView != null) {
            mWeakView.clear();
            mWeakView = null;
        }
    }

    @Override
    public void destroy() {

    }
}