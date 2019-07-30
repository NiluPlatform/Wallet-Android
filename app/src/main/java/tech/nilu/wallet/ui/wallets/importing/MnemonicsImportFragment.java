package tech.nilu.wallet.ui.wallets.importing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

public class MnemonicsImportFragment extends Fragment implements Injectable, WalletPasswordDialogFragment.WalletPasswordClickListener {
    private static final String NETWORK = "Network";

    @Inject
    WalletViewModel viewModel;
    @BindView(R.id.nameText)
    EditText nameText;
    @BindView(R.id.mnemonicWordsText)
    EditText mnemonicsText;

    public MnemonicsImportFragment() {

    }

    public static MnemonicsImportFragment newInstance(long networkId) {
        Bundle args = new Bundle();
        args.putLong(NETWORK, networkId);

        MnemonicsImportFragment fragment = new MnemonicsImportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mnemonics_import, container, false);
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
        String mnemonics = mnemonicsText.getText().toString();
        viewModel.importWallet(mnemonics, walletName, password, getActivity().getFilesDir(), networkId).observe(this, response -> {
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