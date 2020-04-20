package tech.nilu.wallet.ui.tokens;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.web3j.utils.Convert;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.MyStatePagerAdapter;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by root on 3/4/18.
 */

public class TokenDetailsActivity extends BaseActivity implements HasSupportFragmentInjector, AppBarLayout.OnOffsetChangedListener {
    public static final String BALANCE = "Balance";
    public static final String WALLET_ID = "WalletId";
    public static final String CONTRACT_ID = "ContractId";

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 50;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    WalletViewModel viewModel;

    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tvToken)
    TextView tokenText;
    @BindView(R.id.tvAddress)
    TextView addressText;
    @BindView(R.id.tvBalance)
    TextView balanceText;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    private ContractInfo token;
    private int maxScrollSize;
    private boolean isAvatarShown = true;
    private WalletBaseRepository.WalletExtensionData wallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_details);

        token = viewModel.getContractInfo(getIntent().getLongExtra(CONTRACT_ID, 0L));
        initUI();
        initTabs();
    }

    @OnClick(R.id.btnRefreshBalance)
    public void onRefreshClick() {
        ProgressDialog dialog = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        dialog.show();

        viewModel.getERC20Balance(token.getWalletId(), token).observe(this, balance -> {
            if (balance != null && balance.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                dialog.dismiss();

                if (balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    balanceText.setText(String.format("%s: %s %s", getString(R.string.balance), Convert.fromWei(balance.getData().toString(), Convert.Unit.ETHER).toString(), token.getSymbol()));
            }
        });
    }

    @OnClick(R.id.btnCopy)
    public void onCopyClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.app_name), token.getAddress());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Token's address copied", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnDelete)
    public void onDeleteClick() {
        viewModel.deleteContractInfo(token);
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private void initUI() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tokenText.setText(token.getName());
        addressText.setText(token.getAddress());

        BigInteger balance = (BigInteger) getIntent().getSerializableExtra(BALANCE);
        balanceText.setText(String.format("%s: %s %s", getString(R.string.balance), Convert.fromWei(balance.toString(), Convert.Unit.ETHER).toString(), token.getSymbol()));

        appBar.addOnOffsetChangedListener(this);
        maxScrollSize = appBar.getTotalScrollRange();
    }

    private void initTabs() {
        String walletAddress = viewModel.getWalletData(getIntent().getLongExtra(WALLET_ID, 0L)).getCredentials().getAddress();
        MyStatePagerAdapter adapter = new MyStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TokenInformationFragment.newInstance(token.getId()), getString(R.string.contract));
        if (viewModel.canFetchTokenTransfers())
            adapter.addFragment(TokenTransactionsFragment.newInstance(token.getId(), walletAddress), getString(R.string.logs));
        adapter.addFragment(TokenTransferFragment.newInstance(token.getId()), getString(R.string.transfer));
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(pager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / maxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false;
            toolbar.setTitle(token.getName());
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true;
            toolbar.setTitle("");
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
