package br.com.luizfp.segredosufsc.login.confirm_access_code;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.base.BaseActivity;

public class ConfirmAccessCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_access_code);
        setUpToolbar();
        showHomeAsUpEnable();

        if (savedInstanceState == null) {
            Fragment fragment = new ConfirmAccessCodeFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragContainer, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
