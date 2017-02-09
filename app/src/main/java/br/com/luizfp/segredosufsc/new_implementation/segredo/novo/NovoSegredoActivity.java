package br.com.luizfp.segredosufsc.new_implementation.segredo.novo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.base.BaseActivity;
import butterknife.ButterKnife;

public class NovoSegredoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_segredo);
        ButterKnife.bind(this);
        setUpToolbar();
        showHomeAsUpEnable();

        NovoSegredoFragment novoSegredoFragment = (NovoSegredoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragContainer);

        if (novoSegredoFragment == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(
                    R.id.fragContainer,
                    new NovoSegredoFragment()).commit();
        }
    }
}