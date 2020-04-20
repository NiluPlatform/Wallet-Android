package tech.nilu.wallet.ui.send;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.common.NetworksListDialogFragment;

/**
 * Created by root on 1/30/18.
 */

public class SendMoneyActivity extends BaseActivity implements HasSupportFragmentInjector, NetworksListDialogFragment.NetworksListClickListener {
    @Inject
    BaseViewModel viewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private SendMoneyFragment sendMoneyFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        getSupportActionBar().setTitle(getString(R.string.send));
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        //http://nilu.tech/0x8e50150e3861a985542a0caf581bdf35a3f85ddc?data=0x2e1a7d4d0000000000000000000000000000000000000000000000056bc75e2d63100000&amount=0
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            String address = uri.getPath().substring(1);
            String amount = uri.getQueryParameter("amount");
            String token = uri.getQueryParameter("token");
            String networkId = uri.getQueryParameter("network");
            String creator = uri.getQueryParameter("creator");
            String data = uri.getQueryParameter("data");

            sendMoneyFragment = SendMoneyFragment.newInstance(viewModel.getActiveNetwork(), address, amount, token, creator, data);
            getSupportFragmentManager().beginTransaction().replace(R.id.send, sendMoneyFragment).commit();

            if (TextUtils.isEmpty(networkId))
                NetworksListDialogFragment.newInstance().show(getSupportFragmentManager(), null);
            else
                viewModel.activateNetwork(viewModel.getNetwork(Long.parseLong(networkId)));
            sendMoneyFragment.onNetworkChanged(viewModel.getActiveNetwork());
        }
    }

    @Override
    public void onItemClick(NetworksListDialogFragment dialog, Network selectedNetwork) {
        if (sendMoneyFragment != null)
            sendMoneyFragment.onNetworkChanged(selectedNetwork);
        dialog.dismiss();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
