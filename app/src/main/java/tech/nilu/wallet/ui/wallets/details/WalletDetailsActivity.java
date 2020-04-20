package tech.nilu.wallet.ui.wallets.details;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.web3j.crypto.CipherException;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.common.MyPagerAdapter;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.ui.wallets.nns.SearchDomainsActivity;
import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by root on 1/13/18.
 */

public class WalletDetailsActivity extends BaseActivity implements HasSupportFragmentInjector, AppBarLayout.OnOffsetChangedListener, WalletPasswordDialogFragment.WalletPasswordClickListener {
    public static final String BALANCE = "Balance";
    public static final String WALLET_ID = "WalletId";
    public static final String DOMAIN = "Domain";

    private static final int RC_REGISTER_DOMAIN = 1001;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 50;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    WalletViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;

    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tvWallet)
    TextView walletText;
    @BindView(R.id.tvAddress)
    TextView addressText;
    @BindView(R.id.tvBalance)
    TextView balanceText;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    private int maxScrollSize;
    private int selectedMenuItem;
    private boolean isAvatarShown = true;
    private WalletBaseRepository.WalletExtensionData wallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);

        try {
            wallet = viewModel.getWalletData(viewModel.getWallet(getIntent().getLongExtra(WALLET_ID, 0L)),
                    getFilesDir(),
                    baseViewModel.retrieveString(MyKeyStore.PASSWORD));
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }

        initUI();
        initTabs();
    }

    @OnClick(R.id.btnRefreshBalance)
    public void onRefreshClick() {
        ProgressDialog dialog = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        dialog.show();

        viewModel.getBalance(wallet.getCredentials().getAddress()).observe(this, balance -> {
            if (balance != null && balance.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                dialog.dismiss();
                if (balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    balanceText.setText(String.format("%s: %s %s", getString(R.string.balance), Convert.fromWei(balance.getData().toString(), Convert.Unit.ETHER).toString(), viewModel.getNetwork(wallet.getWallet().getNetworkId()).getSymbol()));
            }
        });
    }

    @OnClick(R.id.btnCopy)
    public void onCopyClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.app_name), Eip55.convertToEip55Address(wallet.getCredentials().getAddress()));
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Wallet's address copied", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnMore)
    public void onMoreClick(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.activity_wallet_details, popup.getMenu());
        popup.getMenu().getItem(1).setVisible(wallet.getWallet().getMnemonic() != null);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_register_domain:
                    startActivityForResult(new Intent(this, SearchDomainsActivity.class)
                            .putExtra(SearchDomainsActivity.WALLET_ID, getIntent().getLongExtra(WALLET_ID, 0L)), RC_REGISTER_DOMAIN);
                    return true;
                case R.id.action_export_mnemonics:
                    selectedMenuItem = R.id.action_export_mnemonics;
                    WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                    return true;
                case R.id.action_export_private_key:
                    selectedMenuItem = R.id.action_export_private_key;
                    WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                    return true;
                case R.id.action_export_keystore:
                    selectedMenuItem = R.id.action_export_keystore;
                    WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                    return true;
                case R.id.action_delete:
                    selectedMenuItem = R.id.action_delete;
                    WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                    return true;
            }
            return false;
        });
        popup.show();
    }

    private void initUI() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        walletText.setText(wallet.getWallet().getName());
        addressText.setText(Eip55.convertToEip55Address(wallet.getCredentials().getAddress()));

        BigInteger balance = (BigInteger) getIntent().getSerializableExtra(BALANCE);
        balanceText.setText(String.format("%s: %s %s", getString(R.string.balance), Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString(), viewModel.getNetwork(wallet.getWallet().getNetworkId()).getSymbol()));

        appBar.addOnOffsetChangedListener(this);
        maxScrollSize = appBar.getTotalScrollRange();
    }

    private void initTabs() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(WalletSendFragment.newInstance(wallet.getWallet().getId(), viewModel.getNetwork(wallet.getWallet().getNetworkId()).getSymbol()), getString(R.string.send));
        adapter.addFragment(WalletReceiveFragment.newInstance(wallet.getWallet().getId(), wallet.getCredentials().getAddress()), getString(R.string.receive));
        if (viewModel.canFetchTransactions())
            adapter.addFragment(WalletTransactionsFragment.newInstance(wallet.getCredentials().getAddress(), viewModel.getNetwork(wallet.getWallet().getNetworkId()).getSymbol()), getString(R.string.logs));
        adapter.addFragment(WalletTokensFragment.newInstance(wallet.getWallet().getId()), getString(R.string.tokens));
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(pager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_REGISTER_DOMAIN && resultCode == RESULT_OK && data != null) {
            String domain = data.getStringExtra(DOMAIN);
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage(getString(R.string.successful_domain_registration, domain, domain))
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(getString(R.string.rename), ((dialog, which) -> {
                        viewModel.updateWallet(wallet.getWallet().getId(), domain);
                        dialog.dismiss();
                    })).show();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / maxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false;
            toolbar.setTitle(wallet.getWallet().getName());
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true;
            toolbar.setTitle("");
        }
    }

    @Override
    public void onAccept(WalletPasswordDialogFragment dialog, String password) {
        dialog.dismiss();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        switch (selectedMenuItem) {
            case R.id.action_export_mnemonics:
                intent.putExtra(Intent.EXTRA_TEXT, wallet.getWallet().getMnemonic());
                startActivity(Intent.createChooser(intent, getString(R.string.export_mnemonics)));
                break;
            case R.id.action_export_private_key:
                intent.putExtra(Intent.EXTRA_TEXT, wallet.getCredentials().getEcKeyPair().getPrivateKey().toString(16));
                startActivity(Intent.createChooser(intent, getString(R.string.export_private_key)));
                break;
            case R.id.action_export_keystore:
                intent.putExtra(Intent.EXTRA_TEXT, viewModel.getKeystoreContent(wallet.getWallet(), getFilesDir()));
                startActivity(Intent.createChooser(intent, getString(R.string.export_keystore)));
                break;
            case R.id.action_delete:
                viewModel.deleteWallet(wallet.getWallet());
                setResult(RESULT_OK, new Intent());
                finish();
                break;
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
