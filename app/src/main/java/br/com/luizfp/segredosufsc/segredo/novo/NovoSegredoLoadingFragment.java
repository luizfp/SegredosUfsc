package br.com.luizfp.segredosufsc.segredo.novo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.luizfp.segredosufsc.R;
import br.com.luizfp.segredosufsc.base.BaseFragment;
import br.com.luizfp.segredosufsc.util.L;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovoSegredoLoadingFragment extends BaseFragment {
    private static final String TAG = NovoSegredoLoadingFragment.class.getSimpleName();
    public static final String FRAG_TAG = "loading_fragment";
    @BindView(R.id.loadingView) AVLoadingIndicatorView mLoadingView;
    @BindView(R.id.loadingDone) ImageView mLoadingDone;

    public NovoSegredoLoadingFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * TODO: Isto deveria estar em um presenter separado!
     * Chamado para parar a animação de loading
     *
     * @param event Um evento indicando que o segredo foi enviado com sucesso
     */
    @Subscribe
    public void stopLoadingAnimation(NovoSegredoActivityPresenterImpl.SecretSuccessInsertEvent event) {
        L.d(TAG, "Evento recebido, finalizando animação");
        mLoadingView.animate().alpha(-1f).setDuration(700).start();
        mLoadingDone.setVisibility(View.VISIBLE);
        mLoadingDone.animate().alpha(1f).setDuration(700).start();
    }
}
