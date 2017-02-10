package br.com.luizfp.segredosufsc.new_implementation.segredo.newsecret.presentation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.new_implementation.base.BaseActivity;
import butterknife.ButterKnife;

public class NewSecretActivity extends BaseActivity {

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
    }
}