package tech.nilu.wallet.ui.common;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.injection.Injectable;

/**
 * Created by root on 1/27/18.
 */

public class AddTokenDialogFragment extends DialogFragment implements Injectable {
    private static final String WALLET_ID = "WalletId";

    @BindView(R.id.addressText)
    TextInputEditText addressText;

    private AddTokenClickListener listener;

    public AddTokenDialogFragment() {

    }

    public static AddTokenDialogFragment newInstance(long walletId) {
        Bundle args = new Bundle();
        args.putLong(WALLET_ID, walletId);
        AddTokenDialogFragment fragment = new AddTokenDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.listener = (AddTokenClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_token, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setView(view);

        return builder.create();
    }

    @OnClick(android.R.id.button1)
    public void onAcceptClick() {
        long walletId = getArguments().getLong(WALLET_ID);
        String address = addressText.getText().toString().toLowerCase();
        if (listener != null)
            listener.onAccept(AddTokenDialogFragment.this, walletId, address);
    }

    @OnClick(android.R.id.button2)
    public void onCancelClick() {
        dismiss();
    }

    public interface AddTokenClickListener {
        void onAccept(AddTokenDialogFragment dialog, long walletId, String address);
    }
}
