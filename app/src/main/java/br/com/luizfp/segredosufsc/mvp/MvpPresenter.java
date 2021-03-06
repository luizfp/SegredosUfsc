package br.com.luizfp.segredosufsc.mvp;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by luiz on 3/8/16.
 */
public abstract class MvpPresenter<V extends MvpView> implements Presenter<V> {

    private WeakReference<V> mWeakView;
    private boolean mIsFirstUiCreation;

    @Override
    public void attachView(V view) {
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

    public boolean isFirstUiCreation() {
        return mIsFirstUiCreation;
    }

    public void setIsFirstUiCreation(boolean mIsFirstUiCreation) {
        this.mIsFirstUiCreation = mIsFirstUiCreation;
    }
}
