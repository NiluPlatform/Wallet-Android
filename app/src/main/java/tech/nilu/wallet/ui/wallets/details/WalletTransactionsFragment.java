package tech.nilu.wallet.ui.wallets.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.api.model.transaction.Transaction;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.transactions.TransactionViewModel;
import tech.nilu.wallet.ui.transactions.TransactionsAdapter;
import tech.nilu.wallet.widget.MyScrollListener;

/**
 * Created by root on 1/13/18.
 */

public class WalletTransactionsFragment extends Fragment implements Injectable {
    private static final int FETCH_SIZE = 50;
    private static final String WALLET_ADDRESS = "WalletAddress";
    private static final String NETWORK_SYMBOL = "NetworkSymbol";

    @Inject
    TransactionViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(android.R.id.list)
    RecyclerView list;

    private TransactionsAdapter adapter;
    private MyScrollListener scrollListener;

    public WalletTransactionsFragment() {

    }

    public static WalletTransactionsFragment newInstance(String address, String symbol) {
        Bundle args = new Bundle();
        args.putString(WALLET_ADDRESS, address);
        args.putString(NETWORK_SYMBOL, symbol);

        WalletTransactionsFragment fragment = new WalletTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_transactions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeLayout.post(() -> fetchTransactions(0));
        swipeLayout.setOnRefreshListener(() -> {
            adapter.clear();
            scrollListener.init();
            fetchTransactions(0);
        });
    }

    private void initUI() {
        swipeLayout.setColorSchemeResources(R.color.colorAccent);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        scrollListener = new MyScrollListener(layoutManager, FETCH_SIZE) {
            @Override
            public void onLoadMore(int currentPage) {
                fetchTransactions(currentPage);
            }
        };
        adapter = new TransactionsAdapter(getArguments().getString(NETWORK_SYMBOL));
        list.setLayoutManager(layoutManager);
        list.addItemDecoration(new DividerItemDecoration(getActivity(), layoutManager.getOrientation()));
        list.setAdapter(adapter);
        //list.addOnScrollListener(scrollListener);
    }

    private void fetchTransactions(int page) {
        swipeLayout.setRefreshing(true);
        String me = getArguments().getString(WALLET_ADDRESS);
        viewModel.fetchTransactions(me).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    for (Transaction transaction : response.getData()) {
                        transaction.setSuccess(transaction.getFrom().equals(me));
                        adapter.add(adapter.getItemCount(), transaction);
                    }
                    scrollListener.setLoadMore(response.getData().length >= FETCH_SIZE);
                }
            }
        });
    }
}
