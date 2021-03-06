package tech.nilu.wallet.ui.common;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;

public class AVLIVDialogFragment extends DialogFragment {
    private static final String MESSAGE = "Message";

    /*@BindView(android.R.id.progress)
    NewtonCradleLoading progress;*/
    @BindView(android.R.id.text1)
    TextView messageText;

    public AVLIVDialogFragment() {

    }

    public static AVLIVDialogFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);

        AVLIVDialogFragment fragment = new AVLIVDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_avliv, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        //progress.start();
        //progress.setLoadingColor(R.color.colorAccent);
        messageText.setText(getArguments().getString(MESSAGE));

        return builder.create();
    }
}
