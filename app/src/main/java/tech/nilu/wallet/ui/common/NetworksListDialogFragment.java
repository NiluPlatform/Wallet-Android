package tech.nilu.wallet.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.injection.Injectable;

/**
 * Created by root on 2/19/18.
 */

public class NetworksListDialogFragment extends BottomSheetDialogFragment implements Injectable {
    @Inject
    BaseViewModel viewModel;
    @BindView(android.R.id.list)
    ListView list;

    private ArrayAdapter<Network> adapter;
    private NetworksListClickListener listener;

    public NetworksListDialogFragment() {

    }

    public static NetworksListDialogFragment newInstance() {
        Bundle args = new Bundle();

        NetworksListDialogFragment fragment = new NetworksListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = (NetworksListClickListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setContentView(View.inflate(getContext(), R.layout.dialog_networks_list, null));
        dialog.setCanceledOnTouchOutside(true);
        ButterKnife.bind(this, dialog);

        List<Network> networks = viewModel.getNetworks();
        if (!networks.isEmpty()) viewModel.activateNetwork(networks.get(0));

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, networks);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            if (listener != null) {
                Network selectedNetwork = adapter.getItem(position);
                viewModel.activateNetwork(selectedNetwork);
                listener.onItemClick(NetworksListDialogFragment.this, selectedNetwork);
            }
        });
    }

    @OnClick(android.R.id.button2)
    public void onCancelClick() {
        dismiss();
    }

    public interface NetworksListClickListener {
        void onItemClick(NetworksListDialogFragment dialog, Network selectedNetwork);
    }
}
