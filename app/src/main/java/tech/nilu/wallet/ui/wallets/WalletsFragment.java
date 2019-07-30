package tech.nilu.wallet.ui.wallets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.model.WalletContract;
import tech.nilu.wallet.ui.common.AddTokenDialogFragment;
import tech.nilu.wallet.ui.common.BalanceListener;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.common.NetworkListener;
import tech.nilu.wallet.ui.common.ViewLifecycleFragment;
import tech.nilu.wallet.ui.wallets.contracts.ContractsListActivity;
import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by Navid on 12/14/17.
 */

public class WalletsFragment extends ViewLifecycleFragment implements Injectable, NetworkListener, AddTokenDialogFragment.AddTokenClickListener {
    private static final int RC_ADD_TOKEN = 1001;
    private static final String NETWORK = "Network";

    @Inject
    WalletViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;

    @BindView(R.id.tvNoWallets)
    TextView noWalletsText;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;
    @BindView(android.R.id.list)
    RecyclerView list;

    private Network network;
    private WalletsAdapter adapter;

    public WalletsFragment() {

    }

    public static WalletsFragment newInstance(Network network) {
        Bundle args = new Bundle();
        args.putSerializable(NETWORK, network);

        WalletsFragment fragment = new WalletsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NETWORK, network);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null)
            network = (Network) getArguments().getSerializable(NETWORK);
        else
            network = (Network) savedInstanceState.getSerializable(NETWORK);
        swipeLayout.post(() -> loadWallets(network));
        swipeLayout.setOnRefreshListener(() -> loadWallets(network));
    }

    private void initUI() {
        swipeLayout.setColorSchemeResources(R.color.colorAccent);

        adapter = new WalletsAdapter(walletId -> {
            if (network.getAddress().equals(getString(R.string.nilu_rpc)))
                startActivityForResult(new Intent(getActivity(), ContractsListActivity.class)
                        .putExtra(ContractsListActivity.WALLET_ID, walletId), RC_ADD_TOKEN);
            else {
                AddTokenDialogFragment dialog = AddTokenDialogFragment.newInstance(walletId);
                dialog.setTargetFragment(WalletsFragment.this, 0);
                dialog.show(getFragmentManager(), null);
            }
        });
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    public void loadWallets(Network selectedNetwork) {
        if (!isAdded()) return;
        File parent = getActivity().getFilesDir();
        String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);

        adapter.removeAll();
        adapter.setActiveNetwork(selectedNetwork);
        swipeLayout.setRefreshing(true);
        updateBalance(BigInteger.ZERO);

        viewModel.loadWallets(selectedNetwork.getId(), parent, password).observe(this, response -> {
            if (selectedNetwork.getId() != network.getId()) return;
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    noWalletsText.setVisibility(response.getData() != null && !response.getData().isEmpty() ? View.GONE : View.VISIBLE);

                    List<String> addresses = new ArrayList<>(response.getData().size());
                    for (int i = 0; i < response.getData().size(); i++) {
                        WalletContract wc = response.getData().get(i);
                        addresses.add(wc.getData().getCredentials().getAddress());
                        adapter.add(adapter.getItemCount(), wc.getData(), wc.getContractInfos());
                        viewModel.getBalance(wc.getData().getCredentials().getAddress()).observe(this, balance -> {
                            if (selectedNetwork.getId() != network.getId()) return;
                            if (balance != null) {
                                if (balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                                    adapter.updateItem(wc.getData(), balance.getData());
                                else if (balance.getLiveStatus() == LiveResponseStatus.FAILED)
                                    Toast.makeText(getActivity(), getString(R.string.err_operation_failed), Toast.LENGTH_SHORT).show();
                            }
                        });
                        for (ContractInfo ci : wc.getContractInfos())
                            viewModel.getERC20Balance(wc.getData().getWallet().getId(), ci).observe(this, balance -> {
                                if (selectedNetwork.getId() != network.getId()) return;
                                if (balance != null && balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                                    adapter.updateItemContractBalance(wc.getData(), ci, balance.getData());
                            });
                    }
                    viewModel.getTotalBalance(addresses).observe(this, totalBalance -> {
                        if (selectedNetwork.getId() != network.getId()) return;
                        if (totalBalance != null && totalBalance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                            updateBalance(totalBalance.getData());
                    });
                }
            }
        });
    }

    private void updateBalance(BigInteger balance) {
        if (getActivity() instanceof BalanceListener)
            ((BalanceListener) getActivity()).onBalanceChanged(balance);
    }

    //Will be called after token addition
    private void updateContracts(long walletId) {
        if (!isAdded()) return;
        File parent = getActivity().getFilesDir();
        String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);

        swipeLayout.setRefreshing(true);
        viewModel.loadWallet(walletId, parent, password).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    WalletContract wc = response.getData();
                    adapter.updateItemContracts(wc.getData(), wc.getContractInfos());
                    for (ContractInfo ci : wc.getContractInfos())
                        viewModel.getERC20Balance(wc.getData().getWallet().getId(), ci).observe(this, balance -> {
                            if (balance != null && balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                                adapter.updateItemContractBalance(wc.getData(), ci, balance.getData());
                        });
                }
            }
        });
    }

    //Will be called after wallet creation
    public void loadWallet(long walletId) {
        if (!isAdded()) return;
        File parent = getActivity().getFilesDir();
        String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);

        swipeLayout.setRefreshing(true);
        viewModel.loadWallet(walletId, parent, password).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    noWalletsText.setVisibility(response.getData() != null ? View.GONE : View.VISIBLE);

                    WalletContract wc = response.getData();
                    adapter.add(adapter.getItemCount(), wc.getData(), wc.getContractInfos());
                    viewModel.getBalance(wc.getData().getCredentials().getAddress()).observe(this, balance -> {
                        if (balance != null) {
                            if (balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                                adapter.updateItem(wc.getData(), balance.getData());
                            else if (balance.getLiveStatus() == LiveResponseStatus.FAILED)
                                Toast.makeText(getActivity(), getString(R.string.err_operation_failed), Toast.LENGTH_SHORT).show();
                        }
                    });
                    for (ContractInfo ci : wc.getContractInfos())
                        viewModel.getERC20Balance(wc.getData().getWallet().getId(), ci).observe(this, balance -> {
                            if (balance != null && balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                                adapter.updateItemContractBalance(wc.getData(), ci, balance.getData());
                        });
                }
            }
        });
    }

    //Will be called after wallet backup
    public void updateWallet(long walletId) {
        if (!isAdded()) return;
        File parent = getActivity().getFilesDir();
        String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);

        swipeLayout.setRefreshing(true);
        viewModel.loadWalletData(walletId, parent, password).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                swipeLayout.setRefreshing(false);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    adapter.updateItem(response.getData());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD_TOKEN && resultCode == AppCompatActivity.RESULT_OK && data != null)
            updateContracts(data.getLongExtra(ContractsListActivity.WALLET_ID, 0L));
    }

    @Override
    public void onAccept(AddTokenDialogFragment dialog, long walletId, String address) {
        dialog.dismiss();

        ProgressDialog progress = UIUtils.getProgressDialog(getActivity(), false, getString(R.string.please_wait));
        progress.show();

        ContractInfo ci = new ContractInfo(walletId, address, null, null, "ERC20");
        viewModel.fetchTokenInfo(walletId, ci).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    updateContracts(walletId);
                else
                    Toast.makeText(getActivity(), response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNetworkChanged(Network selectedNetwork) {
        network = selectedNetwork;
        if (getView() != null) loadWallets(network);
    }
}
