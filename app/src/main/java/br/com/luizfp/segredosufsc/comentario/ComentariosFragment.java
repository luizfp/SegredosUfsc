package br.com.luizfp.segredosufsc.comentario;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.wang.avi.AVLoadingIndicatorView;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.mvp.MvpFragment;
import br.com.luizfp.segredosufsc.ui.EmptyRecyclerView;
import br.com.luizfp.segredosufsc.ui.EmptySwipeRefreshLayout;
import br.com.luizfp.segredosufsc.util.L;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// Estamos usando somente a activity agora, isso vai facilitar as coisas pois preciamos usar o
// evento de onBackPressed, que não tem em fragments.
@Deprecated
public class ComentariosFragment extends MvpFragment<ComentariosPresenter>
        implements
        ComentariosAdapter.ComentariosOnClickListener,
        ComentariosView {
    private static final String TAG = ComentariosFragment.class.getSimpleName();
    @BindView(R.id.recycler_comentarios) EmptyRecyclerView mRecyclerComentarios;
    @BindView(R.id.swipeToRefresh) EmptySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.imgBtn_sendComent) ImageButton mImgBtnSendComent;
    @BindView(R.id.edt_coment) EditText mEdtComent;
    @BindView(R.id.progress_comentario) AVLoadingIndicatorView mProgressComentario;
    @BindView(R.id.nestedScrollView_emptyViewComents) ViewGroup mEmptyViewComents;
    private ComentariosAdapter mAdapter;

    public ComentariosFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recoverySegredo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        initSwipeToRefresh();
        observeEditTextComent();
        return view;
    }

    @OnClick(R.id.imgBtn_sendComent)
    public void onClickToSendComent(View view) {
        mPresenter.sendComment(mEdtComent.getText().toString().trim());
    }

    private void recoverySegredo() {
        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey(EXTRA_SEGREDO_ITEM)) {
//            SegredoViewModel segredo = Parcels.unwrap(bundle.getParcelable(EXTRA_SEGREDO_ITEM));
//            mPresenter.setSegredo(segredo);
//        }
    }

    private void initRecyclerView() {
        if (mPresenter.getSegredo() != null) {
            mRecyclerComentarios.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerComentarios.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new ComentariosAdapter(
                    getContext(),
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
    public void showLoading() {
        mImgBtnSendComent.setImageResource(R.drawable.ic_send_gray);
        mImgBtnSendComent.setEnabled(false);
        mEdtComent.setFocusable(false);
        mEdtComent.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_light));
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
        mEdtComent.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        mImgBtnSendComent.setImageResource(R.drawable.ic_send_blue);
        mImgBtnSendComent.setEnabled(true);
    }

    @Override
    public void enableToSendAnotherComment() {
        mEdtComent.setFocusableInTouchMode(true);
        mEdtComent.setText("");
        mEdtComent.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
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

    }

    @Override
    public Context context() {
        return this.getContext().getApplicationContext();
    }
}
