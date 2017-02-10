package br.com.luizfp.segredosufsc.ui.pagination;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.com.luizfp.segredosufsc.new_implementation.base.BaseFragment;
import br.com.luizfp.segredosufsc.util.L;

/**
 * Created by luiz on 3/1/16.
 */
public abstract class PaginationFragment<O,A extends PaginationAdapter> extends BaseFragment
        implements PaginationScrollListener.OnScrollListener {
    protected static final String TAG = PaginationFragment.class.getSimpleName();
    private static final String LOADING_OFFSET = "offset";
    private static final String EXISTS_MORE_DATA = "exists";
    private static final String IS_LOADING = "is_loading";

    /**
     * Offset enviado na busca do webservice
     */
    protected long mOffset;

    /**
     * Variável para controlar se existe mais dados no banco a ser buscado.
     */
    protected boolean mExistsMoreData = true;

    /**
     * O adapter da lista de objetos
     */
    protected A mAdapter;

    /**
     * Variável para controlar se há algum mLoading já em andamento. Não da para usar o
     * método adapter.isLoading() pois ele só informa se há um mLoading de paginação e
     * não um mLoading por outros meios (e.g. pullToRefresh).
     */
    protected boolean mLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recupera o offset e o mExistsMoreData
        if (savedInstanceState != null) {
            mOffset = savedInstanceState.getLong(LOADING_OFFSET);
            mExistsMoreData = savedInstanceState.getBoolean(EXISTS_MORE_DATA);
            mLoading = savedInstanceState.getBoolean(IS_LOADING);
            L.d(TAG, "Recuperando offset: " + mOffset);
            L.d(TAG, "Recuperando mExistsMoreData: " + mExistsMoreData);
            L.d(TAG, "Recuperando mLoading: " + mLoading);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.d(TAG, "Salvando o offset: " + mOffset);
        L.d(TAG, "Salvando o mExistsMoreData: " + mExistsMoreData);
        L.d(TAG, "Salvando o mLoading: " + mLoading);

        // Salva o offset e o mExistsMoreData ao rotacionar a tela
        outState.putLong(LOADING_OFFSET, mOffset);
        outState.putBoolean(EXISTS_MORE_DATA, mExistsMoreData);
        outState.putBoolean(IS_LOADING, mLoading);
    }

    /**
     * Chamado quando o último item da lista estiver sendo visível pelo usuário.
     * Antes de avisar que mais dados precisam ser carregados chamando o método onNewDataNeeded()
     * primeiro é verificado se é preciso carregar mais de dados de fato.
     */
    @Override
    public void onLoadMore() {
        L.d(TAG, "needsToLoadMore(): " + needsToLoadMore());
        if (needsToLoadMore()) {
            onNewDataNeeded();
        }
    }

    /**
     * Chamado quando é necessario carregar mais dados
     */
    public abstract void onNewDataNeeded();

    /**
     * Inicia um recyclerView usando sempre um LinearLayoutManager e já adiciona a esse recycler
     * view a classe responsável por tratar quando o último item do recycler se tornar visível.
     * Os callabacks avisando quando o último item estiver visível serão entregues ao fragment que
     * implementa PaginationScrollListener.OnScrollListener passado por parâmetro.
     *
     * @param recyclerView uma recycler view
     */
    protected void initRecyclerWithPagination(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new PaginationScrollListener(this, linearLayoutManager));
    }


    /**
     * Incrementa o offset somando ele próprio ao limit, que é quanto de informações esperamos
     * de retorno. Dessa maneira, na próxima busca o offset estará correto e não retornará os
     * mesmos itens.
     *
     * Só incrementará o offset se os dados não vieram nulos pois assim não "perdemos"
     * esses itens. Buscas futuras não contariam com eles por conta, justamente, do offset somado.
     *
     * @param newListReceived A nova lista de dados recebida do servidor.
     * @param limit O limit de quantas informações se quer buscar no webservice.
     */
    protected void incrementOffsetByLimitIfListNotNull(List<O> newListReceived, int limit) {
        if (newListReceived != null) {
            mOffset = mOffset + limit;
        }
    }

    /**
     * Chamado para avisar que se está carregando mais informações a partir da paginação.
     */
    protected void onLoadingNewData() {
        setLoading(true);
    }

    /**
     * Avisa o adapter para ele adicionar um loading no fim da lista.
     */
    protected void addAdapterLoadingItem() {
        if (mAdapter != null)
            mAdapter.addLoadingItem();
    }

    /**
     * Avisa o adapter para ele remover o item que tem o progress da lista.
     */
    protected void removeAdapterLoadingItem() {
        if (mAdapter != null)
            mAdapter.removeLoadingItem();
    }

    /**
     * É chamado quando os novos dados são obtidos (utilizando paginação).
     *
     * @param newList Nova lista de dados recebida após o mLoading.
     * @param limit o limit mandando como parametro para o servidor.
     */
    protected void onNewDataReceived(List<O> newList, int limit) {
        setLoading(false);
        verifyIfWeGetAllData(newList, limit);
    }

    /**
     * Útil chamar quando se desejar reinicilar a busca dos dados do zero (e.g. usuário utilizar
     * o pullToRefresh).
     *
     * Reseta o mOffset e o mExistsMoreData
     *
     */
    protected void resetAll() {
        mOffset = 0;
        mExistsMoreData = true;
    }


    /**
     * Informa se a busca atual foi feita através de paginação.
     * Retorna true se o adapter não for nulo e se estiver com o mLoading na tela.
     * Tecnicamente, é o mesmo do que o método isLoading(), porém com outro nome para
     * melhorar o entendimento.
     *
     * @return um boolean informando se é a primeira busca a ser realizada
     */
    protected boolean isPaginationLoading() {
        return mAdapter != null && mAdapter.isLoadingItemShowing();
    }

    /**
     * Retorna true caso já esteja algo em andamento, independente de ser através
     * de paginação ou não.
     *
     * @return um boolean informando se já está em processo de carregamento
     */
    protected boolean isLoading() {
        return mLoading;
    }

    private void setLoading(boolean loading) {
        this.mLoading = loading;
    }

    /**
     * Método utilizado para saber se é preciso buscar mais dados. Retorna true caso dados já
     * não estejam sendo buscados E ainda existam dados para se buscar.
     *
     * @return um boolean informando se é preciso carregar mais dados.
     */
    private boolean needsToLoadMore() {
        return !isLoading() && mExistsMoreData;
    }

    /**
     * Verifica se todos os dados possíveis foram obtidos, se sim, então pode setar o mExistsMoreData
     * para false.
     *
     * @param newListReceived a nova lista de objetos recebida.
     */
    private void verifyIfWeGetAllData(List<O> newListReceived, int limit) {
        // A nova lista recebida do servidor pode ser nula
        L.d(TAG, "Verificando se existe mais dados para receber");
        if (newListReceived != null && newListReceived.size() < limit) {
            L.d(TAG, "Todos os dados foram baixados");
            mExistsMoreData = false;
        } else {
            L.d(TAG, "Ainda existe dados para se baixar");
        }
    }
}
