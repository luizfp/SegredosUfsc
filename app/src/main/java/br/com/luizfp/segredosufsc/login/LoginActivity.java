package br.com.luizfp.segredosufsc.login;

import android.content.Intent;
import android.os.Bundle;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.new_implementation.base.BaseActivity;
import br.com.luizfp.segredosufsc.segredo.activity.SegredosActivity;
import br.com.luizfp.segredosufsc.util.Prefs;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Se usuário já estiver logado abri diretamente a tela de segredos
        if (Prefs.getBoolean(this, Prefs.ESTA_LOGADO_KEY)) {
            startActivity(new Intent(this, SegredosActivity.class));
            finish();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragContainer, new LoginFragment())
                    .commit();
        }
    }
}
