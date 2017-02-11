package br.com.luizfp.segredosufsc.new_implementation.secret.newsecret.presentation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.new_implementation.SegredosUfscApplication;
import br.com.luizfp.segredosufsc.new_implementation.base.BaseActivity;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.HasComponent;
import br.com.luizfp.segredosufsc.new_implementation.secret.DaggerSecretsComponent;
import br.com.luizfp.segredosufsc.new_implementation.secret.SecretsComponent;
import butterknife.ButterKnife;

public class NewSecretActivity extends BaseActivity implements HasComponent<SecretsComponent> {

    private SecretsComponent mSecretsComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_segredo);
        ButterKnife.bind(this);
        setUpToolbar();
        showHomeAsUpEnable();

        NewSecretFragment novoSegredoFragment = (NewSecretFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragContainer);

        if (novoSegredoFragment == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(
                    R.id.fragContainer,
                    new NewSecretFragment()).commit();
        }

        initInjector();
    }

    @Override
    public SecretsComponent getComponent() {
        return mSecretsComponent;
    }

    private void initInjector() {
        mSecretsComponent = DaggerSecretsComponent.builder()
                .secretsRepositoryComponent(((SegredosUfscApplication) getApplication())
                        .getSecretsRepositoyComponent())
                .build();
    }
}