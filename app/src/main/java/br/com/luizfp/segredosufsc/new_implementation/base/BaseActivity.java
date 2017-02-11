package br.com.luizfp.segredosufsc.new_implementation.base;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.new_implementation.SegredosUfscApplication;
import br.com.luizfp.segredosufsc.new_implementation.internal.di.component.ApplicationComponent;

/**
 * Created by luiz on 2/23/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((SegredosUfscApplication) getApplication()).getApplicationComponent();
    }

    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    protected void showHomeAsUpEnable() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void snack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}