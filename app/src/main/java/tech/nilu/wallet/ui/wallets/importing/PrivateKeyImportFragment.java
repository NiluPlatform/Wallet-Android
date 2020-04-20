package tech.nilu.wallet.ui.wallets.importing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.ui.wallets.creation.CreateWalletActivity;
import tech.nilu.wallet.util.UIUtils;

public class PrivateKeyImportFragment extends Fragment implements Injectable, WalletPasswordDialogFragment.WalletPasswordClickListener {
    private static final String NETWORK = "Network";

    @Inject
    WalletViewModel viewModel;
    @BindView(R.id.nameText)
    EditText nameText;
    @BindView(R.id.privateKeyText)
    EditText privateKeyText;

    public PrivateKeyImportFragment() {

    }

    public static PrivateKeyImportFragment newInstance(long networkId) {
        Bundle args = new Bundle();
        args.putLong(NETWORK, networkId);

        PrivateKeyImportFragment fragment = new PrivateKeyImportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_key_import, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.importButton)
    public void onImportClick() {
        WalletPasswordDialogFragment.newInstance().show(getChildFragmentManager(), null);
    }

    @Override
    public void onAccept(WalletPasswordDialogFragment dialog, String password) {
        dialog.dismiss();

        ProgressDialog progress = UIUtils.getProgressDialog(getActivity(), false, getString(R.string.please_wait));
        progress.show();

        long networkId = getArguments().getLong(NETWORK);
        String walletName = nameText.getText().toString();
        String privateKey = privateKeyText.getText().toString();
        viewModel.importWalletFromPrivateKey(privateKey, walletName, password, getActivity().getFilesDir(), networkId).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    getActivity().setResult(AppCompatActivity.RESULT_OK, new Intent().putExtra(CreateWalletActivity.WALLET_ID, response.getData()));
                    getActivity().finish();
                } else
                    Toast.makeText(getActivity(), response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
