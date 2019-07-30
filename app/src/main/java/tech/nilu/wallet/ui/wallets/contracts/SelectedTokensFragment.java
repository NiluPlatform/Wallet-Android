package tech.nilu.wallet.ui.wallets.contracts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class SelectedTokensFragment extends Fragment implements Injectable, ItemSelector {
    private static final String WALLET_ID = "WalletId";

    @Inject
    WalletViewModel viewModel;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @BindView(android.R.id.list)
    RecyclerView list;

    private ContractsAdapter adapter;

    public SelectedTokensFragment() {

    }

    public static SelectedTokensFragment newInstance(long walletId) {
        Bundle args = new Bundle();
        args.putLong(WALLET_ID, walletId);

        SelectedTokensFragment fragment = new SelectedTokensFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_tokens, container, false);
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

        //removeToken(getArguments().getLong(WALLET_ID), "0x7168caa10ad2f2b15db4f95c2fa6ec86ca641b3b");
        adapter = new ContractsAdapter(getContext());
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        list.setAdapter(adapter);
        swipeLayout.post(this::fetchContracts);
    }

    private void initUI() {
        swipeLayout.setEnabled(false);
        swipeLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void removeToken(long walletId, String address) {
        viewModel.removeToken(getArguments().getLong(WALLET_ID), address).observe(this, response -> {
            if (response != null && response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                Log.d("BOOOOOOOOOOOOGH", "removeToken: " + response.getData().getTransactionHash());
            }
        });
    }

    private void fetchContracts() {
        swipeLayout.setRefreshing(true);
        long walletId = getArguments().getLong(WALLET_ID);
        viewModel.fetchSelectedContracts(walletId).observe(this, response -> {
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
                }
            }
        });
    }

    @Override
    public List<ContractInfo> getSelectedItems() {
        return adapter.getSelectedItems();
    }
}
