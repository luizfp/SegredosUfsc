package br.com.luizfp.segredosufsc.mvp;

import android.os.Bundle;

import br.com.luizfp.segredosufsc.base.BaseActivity;

/**
 * Created by luiz on 3/8/16.
 */
public abstract class MvpActivity<P extends Presenter> extends BaseActivity implements MvpView {

    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPresenter();

        if (savedInstanceState == null)
            mPresenter.onUiCreated(true);
        else
            mPresenter.onUiCreated(false);
    }

    private void initPresenter() {
        if (retainPresenter() && getLastCustomNonConfigurationInstance() != null)
            mPresenter = (P) getLastCustomNonConfigurationInstance();
        else
            mPresenter = instantiatePresenter();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (retainPresenter())
            return mPresenter;
        else
            return super.onRetainCustomNonConfigurationInstance();
    }

    protected abstract P instantiatePresenter();

    protected abstract boolean retainPresenter();
}
