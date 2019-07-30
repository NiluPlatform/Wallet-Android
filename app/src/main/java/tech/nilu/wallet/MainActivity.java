package tech.nilu.wallet;

import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.utils.Convert;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.ui.common.BalanceListener;
import tech.nilu.wallet.ui.common.MyStatePagerAdapter;
import tech.nilu.wallet.ui.common.NetworkListener;
import tech.nilu.wallet.ui.faucet.GetAppIdActivity;
import tech.nilu.wallet.ui.faucet.LuckyNiluActivity;
import tech.nilu.wallet.ui.more.AboutActivity;
import tech.nilu.wallet.ui.password.SetPasswordActivity;
import tech.nilu.wallet.ui.receive.ReceiveMoneyFragment;
import tech.nilu.wallet.ui.send.SendMoneyFragment;
import tech.nilu.wallet.ui.tokens.creation.CreateTokenActivity;
import tech.nilu.wallet.ui.wallets.WalletsFragment;
import tech.nilu.wallet.ui.wallets.backup.BackupWalletActivity;
import tech.nilu.wallet.ui.wallets.creation.CreateWalletActivity;
import tech.nilu.wallet.util.ShakeDetector;
import tech.nilu.wallet.widget.NonSwipeableViewPager;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, BalanceListener, ShakeDetector.Listener {
    public static final int RC_BACKUP_WALLET = 1002;
    public static final int RC_WALLET_DETAILS = 1003;
    public static final int RC_TOKEN_DETAILS = 1004;

    private static final int RC_CREATE_WALLET = 1001;
    private static final String SELECTED_NETWORK = "SelectedNetwork";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    MainViewModel viewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.pager)
    NonSwipeableViewPager pager;

    private Network selectedNetwork;
    private ShakeDetector shakeDetector;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_NETWORK, selectedNetwork);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel.createKeys();
        viewModel.generateIdentifier();
        if (!viewModel.hasPassword()) {
            startActivity(new Intent(this, SetPasswordActivity.class));
            finish();
            return;
        }

        if (savedInstanceState == null)
            selectedNetwork = viewModel.activateNetwork(getString(R.string.nilu_rpc));
        else
            selectedNetwork = viewModel.activateNetwork(((Network) savedInstanceState.getSerializable(SELECTED_NETWORK)).getAddress());

        initUI();
        initPager();

        setNetwork(selectedNetwork);
        navigationView.setCheckedItem(R.id.nav_nilu);

        shakeDetector = new ShakeDetector(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        shakeDetector.start((SensorManager) getSystemService(SENSOR_SERVICE));
        if (getIntent() != null && getIntent().getData() != null)
            processUri(getIntent().getData());
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_nilu:
                    setNetwork(viewModel.activateNetwork(getString(R.string.nilu_rpc)));
                    break;
                case R.id.nav_ethereum:
                    setNetwork(viewModel.activateNetwork(getString(R.string.ethereum_rpc)));
                    break;
                case R.id.nav_ropsten:
                    setNetwork(viewModel.activateNetwork(getString(R.string.ropsten_rpc)));
                    break;
                case R.id.nav_pirl:
                    setNetwork(viewModel.activateNetwork(getString(R.string.pirl_rpc)));
                    break;
                case R.id.nav_ether1:
                    setNetwork(viewModel.activateNetwork(getString(R.string.ether1_rpc)));
                    break;
                case R.id.nav_create_wallet:
                    startActivityForResult(new Intent(this, CreateWalletActivity.class)
                            .putExtra(CreateWalletActivity.NETWORK, selectedNetwork.getId()), RC_CREATE_WALLET);
                    break;
                case R.id.nav_create_erc20:
                    if (selectedNetwork.getAddress().equals(getString(R.string.nilu_rpc)))
                        startActivity(new Intent(this, CreateTokenActivity.class));
                    else
                        Toast.makeText(this, getString(R.string.token_creation_works_on_nilu), Toast.LENGTH_LONG).show();
                    break;
                case R.id.nav_get_app_id:
                    startActivity(new Intent(this, GetAppIdActivity.class));
                    break;
                case R.id.nav_about:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initPager() {
        MyStatePagerAdapter adapter = new MyStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SendMoneyFragment.newInstance(selectedNetwork, null, null, null, null, null), getString(R.string.send));
        adapter.addFragment(WalletsFragment.newInstance(selectedNetwork), getString(R.string.wallets));
        adapter.addFragment(ReceiveMoneyFragment.newInstance(selectedNetwork), getString(R.string.receive));
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            pager.setCurrentItem(item.getOrder(), false);
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_wallets);
    }

    private void setNetwork(Network network) {
        selectedNetwork = network;
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tvNetwork)).setText(network.getName());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(network.getName());
            getSupportActionBar().setSubtitle(network.getAddress());
        }

        MyStatePagerAdapter adapter = (MyStatePagerAdapter) pager.getAdapter();
        for (Fragment fragment : adapter.getFragments())
            if (fragment instanceof NetworkListener)
                ((NetworkListener) fragment).onNetworkChanged(selectedNetwork);
    }

    private void processUri(Uri uri) {
        String address = uri.getPath().substring(1);
        String amount = uri.getQueryParameter("amount");
        String token = uri.getQueryParameter("token");
        String data = uri.getQueryParameter("data");

        ((SendMoneyFragment) ((MyStatePagerAdapter) pager.getAdapter()).getItem(0)).sendFromUri(address, amount, token, data);
        bottomNavigationView.setSelectedItemId(R.id.navigation_send);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (pager.getCurrentItem() == 1)
            super.onBackPressed();
        else
            bottomNavigationView.setSelectedItemId(R.id.navigation_wallets);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null)
            switch (requestCode) {
                case RC_CREATE_WALLET:
                    long walletId = data.getLongExtra(CreateWalletActivity.WALLET_ID, 0L);
                    ((WalletsFragment) ((MyStatePagerAdapter) pager.getAdapter()).getItem(1)).loadWallet(walletId);
                    if (data.getBooleanExtra(CreateWalletActivity.BACKUP, false))
                        startActivityForResult(new Intent(this, BackupWalletActivity.class).putExtra(BackupWalletActivity.WALLET_ID, walletId), RC_BACKUP_WALLET);
                    break;
                case RC_BACKUP_WALLET:
                    ((WalletsFragment) ((MyStatePagerAdapter) pager.getAdapter()).getItem(1)).updateWallet(data.getLongExtra(BackupWalletActivity.WALLET_ID, 0L));
                    break;
                case RC_WALLET_DETAILS:
                case RC_TOKEN_DETAILS:
                    ((WalletsFragment) ((MyStatePagerAdapter) pager.getAdapter()).getItem(1)).loadWallets(selectedNetwork);
                    break;
            }
    }

    @Override
    public void onBalanceChanged(BigInteger newBalance) {
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tvBalance)).setText(String.format("%s %s", Convert.fromWei(newBalance.toString(), Convert.Unit.ETHER).toString(), selectedNetwork.getSymbol()));
    }

    @Override
    public void hearShake() {
        startActivity(new Intent(this, LuckyNiluActivity.class));
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
