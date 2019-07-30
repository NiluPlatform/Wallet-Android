package tech.nilu.wallet.ui.send;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.ens.EnsResolver;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.ui.common.NetworkListener;
import tech.nilu.wallet.ui.send.transfer.TransferActivity;
import tech.nilu.wallet.ui.transactions.ReceiptActivity;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by Navid on 12/14/17.
 */

public class SendMoneyFragment extends Fragment implements Injectable, NetworkListener {
    private static final int RC_TRANSFER = 1001;
    private static final int RC_SCANNER = 1002;
    private static final String ADDRESS = "Address";
    private static final String NETWORK = "Network";
    private static final String AMOUNT = "Amount";
    private static final String TOKEN = "Token";
    private static final String CREATOR = "Creator";
    private static final String DATA = "Data";

    @Inject
    WalletViewModel viewModel;
    @BindView(R.id.walletsSpinner)
    Spinner walletsSpinner;
    @BindView(R.id.tokensSpinner)
    Spinner assetsSpinner;
    @BindView(R.id.tvNetworkSymbol)
    TextView networkSymbolText;
    @BindView(R.id.amountLayout)
    TextInputLayout amountLayout;
    @BindView(R.id.amountText)
    TextInputEditText amountText;
    @BindView(R.id.addressLayout)
    TextInputLayout addressLayout;
    @BindView(R.id.addressText)
    TextInputEditText addressText;
    @BindView(R.id.dataLayout)
    TextInputLayout dataLayout;
    @BindView(R.id.dataText)
    TextInputEditText dataText;

    private Network network;
    private ArrayAdapter<Wallet> walletsAdapter;
    private ArrayAdapter<ContractInfo> assetsAdapter;

    public SendMoneyFragment() {

    }

    public static SendMoneyFragment newInstance(Network network, String address, String amount, String token, String creator, String data) {
        Bundle args = new Bundle();
        args.putSerializable(NETWORK, network);
        args.putString(ADDRESS, address);
        args.putString(AMOUNT, amount);
        args.putString(TOKEN, token);
        args.putString(CREATOR, creator);
        args.putString(DATA, data);

        SendMoneyFragment fragment = new SendMoneyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(NETWORK, network);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_money, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        initUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null)
            network = (Network) getArguments().getSerializable(NETWORK);
        else
            network = (Network) savedInstanceState.getSerializable(NETWORK);

        initAdapters();
        loadWallets(network);

        walletsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                loadAssets(walletsAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        assetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ContractInfo asset = assetsAdapter.getItem(position);
                if (asset != null)
                    networkSymbolText.setText(asset.getSymbol());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.destinationsButton)
    public void onDestinationsClick() {
        List<Destination> destinations = viewModel.getDestinations();
        if (destinations.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.err_empty_destinations), Toast.LENGTH_SHORT).show();
            return;
        }
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, destinations);
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.destinations))
                .setAdapter(adapter, (dialog, position) -> {
                    addressText.setText(((Destination) adapter.getItem(position)).getAddress());
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    @OnClick(R.id.transferButton)
    public void onTransferClick() {
        amountLayout.setError(null);
        addressLayout.setError(null);
        if (walletsSpinner.getSelectedItemPosition() == -1) {
            Toast.makeText(getActivity(), getString(R.string.err_no_wallet_specified), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(amountText.getText().toString())) {
            amountLayout.setError(getString(R.string.err_empty_amount));
            return;
        }
        if (TextUtils.isEmpty(addressText.getText().toString())) {
            addressLayout.setError(getString(R.string.err_empty_recipient));
            return;
        }

        long walletId = walletsAdapter.getItem(walletsSpinner.getSelectedItemPosition()).getId();
        String receiver = addressText.getText().toString();
        String amount = amountText.getText().toString();
        String data = dataText.getText().toString();

        if (network.getChainId() == 512 && EnsResolver.isValidEnsName(receiver)) {
            ProgressDialog dialog = UIUtils.getProgressDialog(getActivity(), false, getString(R.string.please_wait));
            dialog.show();

            viewModel.resolve(walletId, receiver).observe(this, response -> {
                if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                    dialog.dismiss();

                    if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                        startTransfer(walletId, response.getData(), amount, data);
                    else
                        Toast.makeText(getActivity(), response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
            startTransfer(walletId, receiver, amount, data);
    }

    private void initUI() {
        amountText.setText(getArguments().getString(AMOUNT));
        addressText.setText(getArguments().getString(ADDRESS));
        dataText.setText(getArguments().getString(DATA));
    }

    private void initAdapters() {
        walletsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        assetsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        walletsSpinner.setAdapter(walletsAdapter);
        assetsSpinner.setAdapter(assetsAdapter);
    }

    public void loadWallets(Network selectedNetwork) {
        String creator = getArguments().getString(CREATOR);
        networkSymbolText.setText(selectedNetwork.getSymbol());
        viewModel.getWalletsLive(selectedNetwork.getId()).observe(this, wallets -> {
            walletsAdapter.clear();
            walletsAdapter.addAll(wallets);
            if (wallets.size() > 0)
                walletsSpinner.setSelection(0);
            else
                assetsAdapter.clear();
            if (!TextUtils.isEmpty(creator))
                for (int i = 0; i < walletsAdapter.getCount(); i++) {
                    WalletBaseRepository.WalletExtensionData data = viewModel.getWalletData(walletsAdapter.getItem(i).getId());
                    if (data != null && data.getCredentials().getAddress().equals(creator)) {
                        walletsSpinner.setSelection(i);
                        break;
                    }
                }
        });
    }

    private void loadAssets(Wallet wallet) {
        if (wallet != null)
            viewModel.getContractInfosLive(wallet.getId()).observe(SendMoneyFragment.this, assets -> {
                ContractInfo primaryAsset = new ContractInfo(wallet.getId(), null, network.getSymbol(), null, null);
                primaryAsset.setSymbol(network.getSymbol());

                assetsAdapter.clear();
                assetsAdapter.add(primaryAsset);
                assetsAdapter.addAll(assets);
                assetsSpinner.setSelection(0);
            });
    }

    private void startTransfer(long walletId, String receiver, String amount, String data) {
        Intent intent = new Intent(getActivity(), TransferActivity.class)
                .putExtra(TransferActivity.WALLET_ID, walletId)
                .putExtra(TransferActivity.ADDRESS, receiver)
                .putExtra(TransferActivity.AMOUNT, amount)
                .putExtra(TransferActivity.DATA, data);
        if (assetsSpinner.getSelectedItemPosition() > 0)
            intent.putExtra(TransferActivity.TOKEN, assetsAdapter.getItem(assetsSpinner.getSelectedItemPosition()));
        startActivityForResult(intent, RC_TRANSFER);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_send_money, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                startActivityForResult(new Intent(getActivity(), ScannerActivity.class), RC_SCANNER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_TRANSFER:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    amountText.setText(null);
                    amountText.requestFocus();
                    addressText.setText(null);
                    dataText.setText(null);
                    if (data != null)
                        startActivity(new Intent(getActivity(), ReceiptActivity.class)
                                .putExtra(ReceiptActivity.NETWORK_SYMBOL, network.getSymbol())
                                .putExtra(ReceiptActivity.HASH, data.getStringExtra(ReceiptActivity.HASH)));
                }
                break;
            case RC_SCANNER:
                if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    amountText.setText(data.getStringExtra(TransferActivity.AMOUNT));
                    addressText.setText(data.getStringExtra(TransferActivity.ADDRESS));
                    dataText.setText(data.getStringExtra(TransferActivity.DATA));
                }
                break;
        }
    }

    public void sendFromUri(String address, String amount, String token, String data) {
        if (amountText != null && addressText != null && dataText != null) {
            amountText.setText(amount);
            addressText.setText(address);
            dataText.setText(data);
        }
    }

    @Override
    public void onNetworkChanged(Network selectedNetwork) {
        network = selectedNetwork;
        if (getView() != null) loadWallets(network);
    }
}
