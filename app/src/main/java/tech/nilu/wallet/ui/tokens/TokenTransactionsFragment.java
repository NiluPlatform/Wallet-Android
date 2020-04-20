package tech.nilu.wallet.ui.tokens;

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
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.transactions.TransactionsAdapter;

/**
 * Created by root on 1/13/18.
 */

public class TokenTransactionsFragment extends Fragment implements Injectable {
    private static final int FETCH_SIZE = 50;
    private static final String TOKEN_ID = "TokenId";
    private static final String WALLET_ADDRESS = "WalletAddress";

    @Inject
    TokenViewModel viewModel;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(android.R.id.list)
    RecyclerView list;

    private ContractInfo token;
    private TransactionsAdapter adapter;

    public TokenTransactionsFragment() {

    }

    public static TokenTransactionsFragment newInstance(long tokenId, String walletAddress) {
        Bundle args = new Bundle();
        args.putLong(TOKEN_ID, tokenId);
        args.putString(WALLET_ADDRESS, walletAddress);

        TokenTransactionsFragment fragment = new TokenTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_transactions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        token = viewModel.getContractInfo(getArguments().getLong(TOKEN_ID));
        initUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeLayout.post(() -> fetchTransactions(0));
        swipeLayout.setOnRefreshListener(() -> {
            adapter.clear();
            fetchTransactions(0);
        });
    }

    private void initUI() {
        swipeLayout.setColorSchemeResources(R.color.colorAccent);

        adapter = new TransactionsAdapter(token.getSymbol());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }

    private void fetchTransactions(int page) {
        swipeLayout.setRefreshing(true);
        String walletAddress = getArguments().getString(WALLET_ADDRESS);
        viewModel.fetchTokenTransfers(token.getAddress(), walletAddress).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    for (Transaction transaction : response.getData()) {
                        transaction.setSuccess(transaction.getFrom().equals(walletAddress));
                        adapter.add(adapter.getItemCount(), transaction);
                    }
            }
        });
    }
}
