package br.com.luizfp.segredosufsc.segredo.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.comentario.ComentarioViewModel;
import br.com.luizfp.segredosufsc.comentario.ComentariosActivity;
import br.com.luizfp.segredosufsc.mvp.MvpPaginationFragment;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;
import br.com.luizfp.segredosufsc.segredo.SegredosAdapter;
import br.com.luizfp.segredosufsc.ui.EmptyRecyclerView;
import br.com.luizfp.segredosufsc.ui.EmptySwipeRefreshLayout;
import br.com.luizfp.segredosufsc.util.IoUtils;
import br.com.luizfp.segredosufsc.util.L;
import br.com.luizfp.segredosufsc.util.SdCardUtils;
import br.com.luizfp.segredosufsc.util.ViewHelper;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class SegredosFragment extends
        MvpPaginationFragment<SegredosFragmentPresenter, SegredoViewModel, SegredosAdapter>
        implements
        SegredosFragmentView,
        SegredosAdapter.SegredoOnClickListener,
        PopupMenu.OnMenuItemClickListener {
    private static final String TAG = SegredosFragment.class.getSimpleName();
    private static final int DEFAULT_IMG_QUALITY = 80;
    public static final String EXTRA_TIPO = "tipo_fragment";
    public static final String TIPO_TODOS = "tipo_todos";
    public static final String TIPO_FAVORITOS = "tipo_favoritos";
    public static final int LIMIT = 30;
    public static final String EXTRA_NEW_COMMENTS_LIST = "new_coments_number";
    public static final String EXTRA_ID_SEGREDO = "id_segredo";
    @BindView(R.id.recycler_segredos) EmptyRecyclerView mRecyclerSegredos;
    @BindView(R.id.swipeToRefresh) EmptySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.progress_segredos) ProgressBar mProgressBar;
    @BindView(R.id.nestedScrollView_emptyViewFavorites) NestedScrollView mEmptyViewFavorites;
    @BindView(R.id.nestedScrollView_emptyViewAllSecrets) NestedScrollView mEmptyViewAllSecrets;
    @BindInt(R.integer.max_lines_segredo) int MAX_LINES_SEGREDOS;

    private View mLayoutSegredoTemp;
    private int mPosicaoAtualAdapter;

    public SegredosFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_TIPO)) {
            String tipoFragment = bundle.getString(EXTRA_TIPO);
            L.d(TAG, "Passando tipo para o presenter: " + tipoFragment);
            mPresenter.setTipo(tipoFragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_segredos, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initRecyclerWithPagination(mRecyclerSegredos);
        initSwypeToRefresh();

        return view;
    }

    private void initSwypeToRefresh() {
        if (mPresenter.getTipo().equals(TIPO_TODOS))
            mSwipeRefreshLayout.setSwipeableChildren(R.id.nestedScrollView_emptyViewAllSecrets, R.id.recycler_segredos);
        else
            mSwipeRefreshLayout.setSwipeableChildren(R.id.nestedScrollView_emptyViewFavorites, R.id.recycler_segredos);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            L.d(TAG, "Algo já carregando ao fazer o swipe: " + isLoading());
            // Reseta offset e existsMoreData
            resetAll();
            mPresenter.onNewDataNeeded(mOffset, false);
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @OnClick(R.id.btn_addFavorites)
    public void onNeedToSwipeToFirstTab(View view) {
        mPresenter.onNeedToSwipeToFirstTab();
    }

    @Override
    protected SegredosFragmentPresenter instantiatePresenter() {
        return new SegredosFragmentPresenterImpl(this);
    }

    @Override
    public void loadingNewItems() {
        L.d(TAG, "Carregando novos itens");
        onLoadingNewData();
    }

    @Override
    public void removeSecretFromFavoritesTab(int position) {
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void addSecretToFavoritesTab(int position) {
        mAdapter.notifyItemInserted(0);
        mRecyclerSegredos.smoothScrollToPosition(0);
    }

    @Override
    public void showMessage(String message, boolean toast) {
        if (toast)
            toast(message);
        else
            snack(mRecyclerSegredos, message);
    }

    @Override
    public void redrawList() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        // Não precisa adicionar o loading para o swipeToRefresh pois é adicionado automaticamente
        // ao fazer o gesto para carregar mais (swipeToRefresh).
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPaginationLoading() {
        L.d(TAG, "Mostrando pagination loading");
        addAdapterLoadingItem();
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String errorMessage) {
        snack(mRecyclerSegredos, errorMessage);
    }

    @Override
    public Context context() {
        return getContext().getApplicationContext();
    }

    @Override
    public void showData(List<SegredoViewModel> segredosList) {
        onNewDataReceived(segredosList, LIMIT);
        incrementOffsetByLimitIfListNotNull(segredosList, LIMIT);
        if (isPaginationLoading()) {
            removeAdapterLoadingItem();
        } else {
            mAdapter = new SegredosAdapter(getContext(), segredosList, this);
            mRecyclerSegredos.setAdapter(mAdapter);
            if (mPresenter.getTipo().equals(TIPO_FAVORITOS)) {
                mRecyclerSegredos.setEmptyView(mEmptyViewFavorites);
            } else {
                mRecyclerSegredos.setEmptyView(mEmptyViewAllSecrets);
            }
        }
    }

    @Override
    public void onNewDataNeeded() {
        mPresenter.onNewDataNeeded(mOffset, true);
    }

    @Override
    public void onClickItem(int position, TextView txtSegredo) {
        // TODO: Animação para expandir e colapsar o texto do segredo
        expandCollapseText(txtSegredo);
    }

    @Override
    public void onClickToFavorite(int position, boolean addToFavorite) {
        mPresenter.onClickToFavorite(position, addToFavorite);
    }

    @Override
    public void onClickToSeeComents(int position) {
        L.d(TAG, "onClickToSeeComents(): " + position);
        SegredoViewModel segredo = mPresenter.getSegredo(position);
        Intent intent = new Intent(getActivity(), ComentariosActivity.class);
        intent.putExtra(ComentariosActivity.EXTRA_SEGREDO_ITEM, Parcels.wrap(segredo));
        startActivityForResult(intent, 0);
        getActivity().overridePendingTransition(R.anim.slide_up, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras().containsKey(EXTRA_ID_SEGREDO)) {
                Long idSegredo = data.getLongExtra(EXTRA_ID_SEGREDO, -1);
                List<ComentarioViewModel> comments = Parcels.unwrap(data.getParcelableExtra(EXTRA_NEW_COMMENTS_LIST));
                if (idSegredo > 0 && comments != null) {
                    L.d(TAG, "Id recebido: " + idSegredo);
                    mPresenter.updateNumberOfComments(idSegredo, comments);
                }
            }
        }
    }

    @Override
    public void onClickToShare(int position, View shareView, View layoutSegredo) {
        this.mLayoutSegredoTemp = layoutSegredo;
        this.mPosicaoAtualAdapter = position;
        PopupMenu popupMenu = new PopupMenu(getContext(), shareView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu_share_secret);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_img:
                shareImageSegredo();
                break;
            case R.id.action_text:
                shareTextSegredo();
                break;
            case R.id.action_save_gallery:
                saveToGallery();
                break;
        }
        return false;
    }

    private void shareImageSegredo() {
        // TODO: mesmo caso do método saveToGallery().
        Bitmap bitmap = ViewHelper.getBitmap(mLayoutSegredoTemp);
        if (bitmap != null) {
            // Cria o arquivo no SD card
            File file = SdCardUtils.getPrivateFile(
                    getContext(),
                    "imageToShare",
                    Environment.DIRECTORY_PICTURES);

            // Salva o bitmap no arquivo
            IoUtils.writeBitmap(file, bitmap);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Compartilhar imagem com:"));
        } else {
            snack(mRecyclerSegredos, "Erro ao tentar compartilhar foto, tente novamente");
        }
    }

    private void shareTextSegredo() {
        SegredoViewModel segredo = mPresenter.getSegredo(mPosicaoAtualAdapter);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, segredo.getSegredo());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Compartilhar texto com:"));
    }

    private void saveToGallery() {
        // TODO: Isso é sem dúvida um caso de uso e o tratamento natural seria criar para ele uma
        // classe única (que representa esse caso de uso para fazer esse salvamento em background)
        // porém o modo como é salvo é exclusivamente uma lógica da View e não me parece certo levar
        // isso para o baixo nível. Talvez o mais certo seja fazer esse salvamente aqui mesmo na
        // view só que usando Rx (em background). Uma quantidade de lógica aceitável para se ter
        // aqui.
        // FIXME: Se tentar salvar imagens com mesmo nome dá problema.
        // Por isso o System.currentTimeMillis()
        boolean b = ViewHelper.saveToGallery(mLayoutSegredoTemp,
                "segredo_" + System.currentTimeMillis(), DEFAULT_IMG_QUALITY);
        L.d(TAG, "saveToGallery(): " + b);
        if (b) {
            toast("Salvo com sucesso");
        } else {
            toast("Erro ao salvar imagem, tente novamente");
        }
    }

    private void expandCollapseText(TextView txtSegredo) {
        if (TextViewCompat.getMaxLines(txtSegredo) == MAX_LINES_SEGREDOS) {
            txtSegredo.setMaxLines(Integer.MAX_VALUE);
            txtSegredo.setEllipsize(null);
        } else {
            txtSegredo.setMaxLines(MAX_LINES_SEGREDOS);
            txtSegredo.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}