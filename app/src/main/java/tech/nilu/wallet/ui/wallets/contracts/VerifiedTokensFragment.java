package tech.nilu.wallet.ui.wallets.contracts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.widget.MyScrollListener;

public class VerifiedTokensFragment extends Fragment implements Injectable, ItemSelector {
    private static final int FETCH_SIZE = 50;
    private static final String WALLET_ID = "WalletId";

    @Inject
    WalletViewModel viewModel;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @BindView(android.R.id.list)
    RecyclerView list;

    private ContractsAdapter adapter;
    private MyScrollListener scrollListener;

    public VerifiedTokensFragment() {

    }

    public static VerifiedTokensFragment newInstance(long walletId) {
        Bundle args = new Bundle();
        args.putLong(WALLET_ID, walletId);

        VerifiedTokensFragment fragment = new VerifiedTokensFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verified_tokens, container, false);
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

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        scrollListener = new MyScrollListener(layout, FETCH_SIZE) {
            @Override
            public void onLoadMore(int currentPage) {
                fetchContracts(currentPage, FETCH_SIZE);
            }
        };
        adapter = new ContractsAdapter(getContext());
        list.setLayoutManager(layout);
        list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        list.setAdapter(adapter);
        list.addOnScrollListener(scrollListener);
        swipeLayout.post(() -> {
            scrollListener.init();
            fetchContracts(0, FETCH_SIZE);
        });
    }

    private void initUI() {
        swipeLayout.setEnabled(false);
        swipeLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void fetchContracts(int offset, int length) {
        swipeLayout.setRefreshing(true);
        long walletId = getArguments().getLong(WALLET_ID);
        viewModel.fetchVerifiedContracts(walletId, offset, length).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    for (ContractInfo contractInfo : response.getData())
                        adapter.add(adapter.getItemCount(), contractInfo);

                    for (ContractInfo contractInfo : viewModel.getContractInfos(walletId)) {
                        int pos = adapter.indexOf(contractInfo);
                        if (pos > -1)
                            adapter.setSelected(pos, true);
                    }
                    scrollListener.setLoadMore(response.getData().size() > 0);
                }
            }
        });
    }

    @Override
    public List<ContractInfo> getSelectedItems() {
        return adapter.getSelectedItems();
    }
}
