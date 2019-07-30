package tech.nilu.wallet.ui.wallets.creation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;

/**
 * Created by root on 1/27/18.
 */

public class PasswordDialogFragment extends DialogFragment {
    @BindView(R.id.passwordText)
    TextInputEditText passwordText;

    private PasswordClickListener listener;

    public PasswordDialogFragment() {

    }

    public static PasswordDialogFragment newInstance() {
        Bundle args = new Bundle();
        PasswordDialogFragment fragment = new PasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = (PasswordClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_password, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setView(view);

        return builder.create();
    }

    @OnClick(android.R.id.button1)
    public void onAcceptClick() {
        String password = passwordText.getText().toString();
        if (listener != null)
            listener.onAccept(PasswordDialogFragment.this, password);
    }

    @OnClick(android.R.id.button2)
    public void onCancelClick() {
        dismiss();
    }

    public interface PasswordClickListener {
        void onAccept(PasswordDialogFragment dialog, String password);
    }
}
