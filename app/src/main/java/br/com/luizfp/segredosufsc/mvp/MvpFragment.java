package br.com.luizfp.segredosufsc.mvp;

import android.os.Bundle;

import br.com.luizfp.segredosufsc.new_implementation.base.BaseFragment;

/**
 * Created by luiz on 3/11/16.
 */
public abstract class MvpFragment<P extends Presenter> extends BaseFragment implements MvpView {

    protected P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Se o Fragment setar o setRetainInstance para true, então, obviamente, o onCreate()
         * não será chamado em uma mudança de configuração (e.g. rotação da tela) e assim nosso
         * presenter sobrevive a essas mudanças :D
         */
        mPresenter = instantiatePresenter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null)
            mPresenter.onUiCreated(true);
        else
            mPresenter.onUiCreated(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    protected abstract P instantiatePresenter();
}
