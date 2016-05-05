package br.com.luizfp.segredosufsc.segredo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.login.LoginActivity;
import br.com.luizfp.segredosufsc.mvp.MvpActivity;
import br.com.luizfp.segredosufsc.segredo.fragment.SegredosFragment;
import br.com.luizfp.segredosufsc.segredo.novo.NovoSegredoActivity;
import br.com.luizfp.segredosufsc.ui.fragments.dialog.SelectLetterDialog;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.Prefs;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SegredosActivity extends MvpActivity<SegredosActivityPresenter>
        implements
        SegredosActivityView, SelectLetterDialog.OnSelectLetra,
        TabLayout.OnTabSelectedListener {
    private static final String TAG = SegredosActivity.class.getSimpleName();
    public static final String EXTRA_LETTER_SELECTED = "extra_selected_letter";
    private static final int NUM_PAGES = 2;
    private MenuItem mMenuItemLetra;
    @Bind(R.id.viewPager) ViewPager mViewPager;
    private char mCurrentLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segredos);
        ButterKnife.bind(this);
        setUpToolbar();

        initViewPager();

        // Recupera o passado da tela de login apenas uma vez
        L.d(TAG, "savedInstanceState: " + savedInstanceState);
        if (savedInstanceState == null)
            recoveryExtraLetterSelected(getIntent().getExtras());
        else
            recoveryExtraLetterSelected(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.d(TAG, "Salvando letra: " + mCurrentLetter);
        outState.putChar(EXTRA_LETTER_SELECTED, mCurrentLetter);
    }

    @Override
    protected SegredosActivityPresenter instantiatePresenter() {
        return new SegredosActivityPresenterImpl(this);
    }

    @Override
    protected boolean retainPresenter() {
        return false;
    }

    private void recoveryExtraLetterSelected(Bundle bundle) {
        if (bundle != null && bundle.containsKey(EXTRA_LETTER_SELECTED)) {
            mCurrentLetter = bundle.getChar(EXTRA_LETTER_SELECTED);
            L.d(TAG, "Nova letra: " + mCurrentLetter);
        } else {
            // Se não contém a letra, talvez seja porque o usuário abriu o app
            // diretamente nessa tela (já estava logado) e então não foi enviado na
            // intent
            mCurrentLetter = Prefs.getChar(this, Prefs.INICIAL_USER_KEY);
        }
    }

    private void initViewPager() {
        mViewPager.setAdapter(new SegredosTabsAdapter(getSupportFragmentManager()));
        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // Adiciona as tabs
        tabLayout.addTab(tabLayout.newTab().setText(R.string.text_todos));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.text_favoritos));
        // Listener para tratar eventos ao clique na tab
        tabLayout.setOnTabSelectedListener(this);
        // Se mudar o ViewPager atualiza o indicador na tab
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // Se alterar tab, atualiza o mViewPager
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        L.d(TAG, "Criando o menu");
        getMenuInflater().inflate(R.menu.menu_segredos, menu);
        this.mMenuItemLetra = menu.findItem(R.id.action_inicial_nome);
        // Aqui o menu está pronto, então podemos setar a letra
        changeLetterInMenu(mCurrentLetter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_inicial_nome) {
            showDialog(getSupportFragmentManager());
            return true;
        } else if (id == R.id.action_about) {
            startAboutActivity();
            return true;
        } else if (id == R.id.action_exit) {
            Prefs.putBoolean(this, Prefs.ESTA_LOGADO_KEY, false);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAboutActivity() {
        new LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withActivityTitle("Sobre")
                .withAboutSpecial1("Ícones")
                .withAboutSpecial1Description("<b>Todos os icones nesse aplicativo foram obtidos " +
                        "em:</b><br/><br/> <a href=\"http://www.flaticon.com/\">FLATICON</a>")
                .withAboutSpecial2("Github")
                .withAboutSpecial2Description("<b>Você pode acessar o código no github " +
                        "(e contribuir, se quiser):</b><br/><br/> <a href=\"http://www.flaticon.com" +
                        "/\">GitHub segredos ufsc</a>")
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withExcludedLibraries("fastadapter", "AndroidIconics", "NineOldAndroids")
                .withAboutDescription("Esse é um projeto Open Source que tem como objetivo usar " +
                        "as práticas que atualmente são o estado da arte no desenvolvimento Android.<br/> " +
                        "Por esse motivo esse projeto faz uso de RxJava e MVP, por exemplo.<br/><br/> " +
                        "<b>A ideia desse aplicativo não é original e ele é totalmente inspirado " +
                        "na já conhecida página do Segredos UFSC no facebook:</b><br/><br/> " +
                        "<a href=\"https://www.facebook.com/segredosuniversitarios/\">Facebook Segredos UFSC</a><br/> " +
                        "Desse modo, todos os créditos da ideia em si se devem aos criadores da página.")
                .start(this);
    }

    private void showDialog(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(SelectLetterDialog.DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFrag = SelectLetterDialog.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(SelectLetterDialog.LETRA_ATUAL,
                (mMenuItemLetra != null) ? mMenuItemLetra.getTitle().toString() : "");
        dialogFrag.setArguments(bundle);
        dialogFrag.show(ft, SelectLetterDialog.DIALOG_TAG);
    }

    @OnClick(R.id.fab_novoSegredo)
    public void onClickFab(View view) {
        startActivity(new Intent(view.getContext(), NovoSegredoActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onNewLetraSelected(char letra) {
        L.d(TAG, "Uma nova letra foi selecionada: " + letra);
        mPresenter.onLetterChanged(letra);
        mCurrentLetter = letra;
    }

    @Override
    public void onSameLetraSelected() {
        toast("Letra não alterada");
    }

    @Override
    public void changeLetterInMenu(char newLetra) {
        L.d(TAG, "changeLetraImenu() letra: " + newLetra);
        L.d(TAG, "changeLetraImenu() mMenuItemLetra: " + mMenuItemLetra);
        if (mMenuItemLetra != null) {
            mMenuItemLetra.setTitle(String.valueOf(newLetra));
        }
    }

    @Override
    public void showError(String errorMessage) {
        snack(mViewPager, errorMessage);
    }

    @Override
    public void swipeToFirstTab() {
        mViewPager.setCurrentItem(0, true);
    }

    @Override
    public void showLetterChangedWithSuccess(String message) {
        toast(message);
    }

    @Override
    public Context context() {
        return this.getApplicationContext();
    }

    private class SegredosTabsAdapter extends FragmentPagerAdapter {

        public SegredosTabsAdapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            if (position == 0) {
                bundle.putString(SegredosFragment.EXTRA_TIPO, SegredosFragment.TIPO_TODOS);
            } else {
                bundle.putString(SegredosFragment.EXTRA_TIPO, SegredosFragment.TIPO_FAVORITOS);
            }

            Fragment fragment = new SegredosFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
