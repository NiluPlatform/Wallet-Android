package tech.nilu.wallet.ui.wallets.details;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.ens.EnsResolver;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.Destination;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.send.transfer.TransferActivity;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by root on 1/13/18.
 */

public class WalletSendFragment extends Fragment implements Injectable {
    private static final int RC_TRANSFER = 1001;
    private static final String WALLET_ID = "WalletId";
    private static final String NETWORK_SYMBOL = "NetworkSymbol";

    @Inject
    WalletViewModel viewModel;
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

    public WalletSendFragment() {

    }

    public static WalletSendFragment newInstance(long id, String symbol) {
        Bundle args = new Bundle();
        args.putLong(WALLET_ID, id);
        args.putString(NETWORK_SYMBOL, symbol);

        WalletSendFragment fragment = new WalletSendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_send, container, false);
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
        if (TextUtils.isEmpty(amountText.getText().toString())) {
            amountLayout.setError(getString(R.string.err_empty_amount));
            return;
        }
        if (TextUtils.isEmpty(addressText.getText().toString())) {
            addressLayout.setError(getString(R.string.err_empty_recipient));
            return;
        }

        String receiver = addressText.getText().toString();
        String amount = amountText.getText().toString();

        if (viewModel.getActiveNetwork().getChainId() == 512 && EnsResolver.isValidEnsName(receiver)) {
            ProgressDialog dialog = UIUtils.getProgressDialog(getActivity(), false, getString(R.string.please_wait));
            dialog.show();

            viewModel.resolve(getArguments().getLong(WALLET_ID), receiver).observe(this, response -> {
                if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                    dialog.dismiss();

                    if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                        startActivityForResult(new Intent(getActivity(), TransferActivity.class)
                                .putExtra(TransferActivity.WALLET_ID, getArguments().getLong(WALLET_ID))
                                .putExtra(TransferActivity.ADDRESS, response.getData())
                                .putExtra(TransferActivity.AMOUNT, amount), RC_TRANSFER);
                    else
                        Toast.makeText(getActivity(), response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
            startActivityForResult(new Intent(getActivity(), TransferActivity.class)
                    .putExtra(TransferActivity.WALLET_ID, getArguments().getLong(WALLET_ID))
                    .putExtra(TransferActivity.ADDRESS, receiver)
                    .putExtra(TransferActivity.AMOUNT, amount), RC_TRANSFER);
    }

    private void initUI() {
        networkSymbolText.setText(getArguments().getString(NETWORK_SYMBOL));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_TRANSFER && resultCode == AppCompatActivity.RESULT_OK)
            getActivity().finish();
    }
}
