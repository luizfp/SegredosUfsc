package br.com.luizfp.segredosufsc.comentario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.wang.avi.AVLoadingIndicatorView;

import org.parceler.Parcels;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.mvp.MvpActivity;
import br.com.luizfp.segredosufsc.ui.EmptyRecyclerView;
import br.com.luizfp.segredosufsc.ui.EmptySwipeRefreshLayout;
import br.com.luizfp.segredosufsc.segredo.SegredoViewModel;
import br.com.luizfp.segredosufsc.segredo.fragment.SegredosFragment;
import br.com.luizfp.segredosufsc.util.L;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComentariosActivity extends MvpActivity<ComentariosPresenter>
        implements
        ComentariosAdapter.ComentariosOnClickListener,
        ComentariosView {
    public static final String EXTRA_SEGREDO_ITEM = "segredo_item";
    private static final String TAG = ComentariosActivity.class.getSimpleName();
    @Bind(R.id.recycler_comentarios) EmptyRecyclerView mRecyclerComentarios;
    @Bind(R.id.swipeToRefresh) EmptySwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.imgBtn_sendComent) ImageButton mImgBtnSendComent;
    @Bind(R.id.edt_coment) EditText mEdtComent;
    @Bind(R.id.progress_comentario) AVLoadingIndicatorView mProgressComentario;
    @Bind(R.id.nestedScrollView_emptyViewComents) ViewGroup mEmptyViewComents;
    private ComentariosAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        ButterKnife.bind(this);
        setUpToolbar();
        recoveryIntentData();
        initRecyclerView();
        initSwipeToRefresh();
        observeEditTextComent();
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.thereIsANewNumberOfComments()) {
            SegredoViewModel segredo = mPresenter.getSegredo();
            L.d(TAG, "Id enviado: " + segredo.getId());
            L.d(TAG, "Novo número de comentários: " + segredo.getComentarioList().size());
            Intent intent = new Intent();
            intent.putExtra(SegredosFragment.EXTRA_NEW_COMMENTS_LIST, Parcels.wrap(segredo.getComentarioList()));
            intent.putExtra(SegredosFragment.EXTRA_ID_SEGREDO, segredo.getId());
            setResult(RESULT_OK, intent);
        } else {
            L.d(TAG, "Número de comentários não alterado");
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @OnClick(R.id.imgBtn_sendComent)
    public void onClickToSendComent(View view) {
        mPresenter.sendComment(mEdtComent.getText().toString().trim());
    }

    private void recoveryIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(EXTRA_SEGREDO_ITEM)) {
            SegredoViewModel segredo = Parcels.unwrap(bundle.getParcelable(EXTRA_SEGREDO_ITEM));
            mPresenter.setSegredo(segredo);
        }
    }

    private void initRecyclerView() {
        if (mPresenter.getSegredo() != null) {
            mRecyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerComentarios.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new ComentariosAdapter(
                    this,
                    mPresenter.getSegredo().getComentarioList(),
                    this);
            mRecyclerComentarios.setAdapter(mAdapter);
            mRecyclerComentarios.setEmptyView(mEmptyViewComents);
        }
    }

    private void initSwipeToRefresh() {
        mSwipeRefreshLayout.setSwipeableChildren(R.id.nestedScrollView_emptyViewComents, R.id.recycler_comentarios);
        mSwipeRefreshLayout.setOnRefreshListener(mPresenter::updateComentariosList);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private void observeEditTextComent() {
        RxTextView.textChangeEvents(mEdtComent).subscribe(t -> {
            if (t.text().toString().trim().isEmpty()) {
                L.d(TAG, "Desabilitou envio");
                mImgBtnSendComent.setImageResource(R.drawable.ic_send_gray);
                mImgBtnSendComent.setEnabled(false);
            } else {
                L.d(TAG, "Habilitou envio");
                mImgBtnSendComent.setImageResource(R.drawable.ic_send_blue);
                mImgBtnSendComent.setEnabled(true);
            }
        });
    }

    @Override
    public void onClickItem(int position, TextView txtComentario) {

    }

    @Override
    protected ComentariosPresenter instantiatePresenter() {
        return new ComentariosPresenterImpl(this);
    }

    @Override
    protected boolean retainPresenter() {
        return true;
    }

    @Override
    public void showLoading() {
        mImgBtnSendComent.setImageResource(R.drawable.ic_send_gray);
        mImgBtnSendComent.setEnabled(false);
        mEdtComent.setFocusable(false);
        mEdtComent.setTextColor(ContextCompat.getColor(this, R.color.gray_light));
        mProgressComentario.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressComentario.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String errorMessage) {
        toast(errorMessage);
    }

    @Override
    public void enableToResend() {
        mEdtComent.setFocusableInTouchMode(true);
        mEdtComent.setTextColor(ContextCompat.getColor(this, R.color.black));
        mImgBtnSendComent.setImageResource(R.drawable.ic_send_blue);
        mImgBtnSendComent.setEnabled(true);
    }

    @Override
    public void enableToSendAnotherComment() {
        mEdtComent.setFocusableInTouchMode(true);
        mEdtComent.setText("");
        mEdtComent.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    public void showSendWithSuccess(String message) {
        toast(message);
    }

    @Override
    public void updateComentariosList() {
        L.d(TAG, "Atualizando lista de comentários");
        mAdapter.setNewList(mPresenter.getSegredo().getComentarioList());
    }

    @Override
    public void redrawList() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Context context() {
        return this.getApplicationContext();
    }
}
