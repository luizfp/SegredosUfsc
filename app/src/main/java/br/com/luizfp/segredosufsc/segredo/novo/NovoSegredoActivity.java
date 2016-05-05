package br.com.luizfp.segredosufsc.segredo.novo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.mvp.MvpActivity;
import butterknife.ButterKnife;

public class NovoSegredoActivity extends MvpActivity<NovoSegredoActivityPresenter>
        implements
        NovoSegredoFragment.OnClickToSend,
        NovoSegredoActivityView {
    public static final String EXTRA_NOVO_SEGREDO = "extra_novo_segredo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_segredo);
        ButterKnife.bind(this);
        setUpToolbar();
        showHomeAsUpEnable();

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragContainer, new NovoSegredoFragment(), NovoSegredoFragment.FRAG_TAG).commit();
        }
    }

    @Override
    protected NovoSegredoActivityPresenter instantiatePresenter() {
        return new NovoSegredoActivityPresenterImpl(this);
    }

    @Override
    protected boolean retainPresenter() {
        return true;
    }

    @Override
    public Context context() {
        return this.getApplicationContext();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onSend(String segredo) {
        mPresenter.sendNewSecret(segredo);
    }

    @Override
    public void addLoadingFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragContainer, new NovoSegredoLoadingFragment(), NovoSegredoLoadingFragment.FRAG_TAG)
                .commit();
    }

    @Override
    public void removeLoadingFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment loadingFragment = fm.findFragmentByTag(NovoSegredoLoadingFragment.FRAG_TAG);
        fm.beginTransaction().remove(loadingFragment).commit();
    }

    @Override
    public void showHideNovoSegredoFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment novoSegredoFragment = fm.findFragmentByTag(NovoSegredoFragment.FRAG_TAG);
        if (novoSegredoFragment.isHidden()) {
            fm.beginTransaction().show(novoSegredoFragment).commit();
        } else {
            fm.beginTransaction().hide(novoSegredoFragment).commit();
        }
    }

    @Override
    public void showError(String errorMessage) {
        toast(errorMessage);
    }

    @Override
    public void showLoadingToolbarMessage() {
        changeToolbarTitle("Enviando…");
    }

    @Override
    public void showSuccessToolbarMessage() {
        changeToolbarTitle("Pronto!");
    }

    @Override
    public void resetToolbarMessage() {
        changeToolbarTitle(getString(R.string.text_compartilhe_segredo));
    }
}
