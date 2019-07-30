package tech.nilu.wallet.ui.password;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.util.MyKeyStore;

/**
 * Created by root on 1/27/18.
 */

public class WalletPasswordDialogFragment extends DialogFragment implements Injectable {
    @Inject
    BaseViewModel viewModel;
    @BindView(R.id.passwordTextLayout)
    TextInputLayout passwordTextLayout;
    @BindView(R.id.passwordText)
    TextInputEditText passwordText;

    private WalletPasswordClickListener listener;

    public WalletPasswordDialogFragment() {

    }

    public static WalletPasswordDialogFragment newInstance() {
        Bundle args = new Bundle();
        WalletPasswordDialogFragment fragment = new WalletPasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = (WalletPasswordClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            try {
                this.listener = (WalletPasswordClickListener) getParentFragment();
            } catch (ClassCastException ee) {
                ee.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_wallet_password, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setView(view);

        return builder.create();
    }

    @OnClick(android.R.id.button1)
    public void onAcceptClick() {
        if (listener != null) {
            passwordTextLayout.setError(null);

            String password = passwordText.getText().toString();
            if (viewModel.retrieveString(MyKeyStore.PASSWORD).equals(password))
                listener.onAccept(WalletPasswordDialogFragment.this, password);
            else
                passwordTextLayout.setError(getString(R.string.err_incorrect_password));
        }
    }

    @OnClick(android.R.id.button2)
    public void onCancelClick() {
        dismiss();
    }

    public interface WalletPasswordClickListener {
        void onAccept(WalletPasswordDialogFragment dialog, String password);
    }
}
