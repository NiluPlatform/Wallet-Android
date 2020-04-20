package tech.nilu.wallet.ui.wallets.contracts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.MyStatePagerAdapter;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.UIUtils;

/**
 * Created by root on 1/9/18.
 */

public class ContractsListActivity extends BaseActivity implements HasSupportFragmentInjector {
    public static final String WALLET_ID = "WalletId";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    WalletViewModel viewModel;

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts_list);
        ButterKnife.bind(this);

        initTabs();
    }

    private void initTabs() {
        MyStatePagerAdapter adapter = new MyStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SelectedTokensFragment.newInstance(getIntent().getLongExtra(WALLET_ID, 0L)), getString(R.string.top_tokens));
        adapter.addFragment(VerifiedTokensFragment.newInstance(getIntent().getLongExtra(WALLET_ID, 0L)), getString(R.string.verified_tokens));
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contracts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveContracts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveContracts() {
        ProgressDialog progress = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        progress.show();

        List<ContractInfo> selectedContracts = new ArrayList<>();
        for (Fragment f : getSupportFragmentManager().getFragments())
            if (f instanceof ItemSelector)
                selectedContracts.addAll(((ItemSelector) f).getSelectedItems());
        long walletId = getIntent().getLongExtra(WALLET_ID, 0L);

        viewModel.saveContracts(walletId, selectedContracts).observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    setResult(RESULT_OK, new Intent().putExtra(ContractsListActivity.WALLET_ID, getIntent().getLongExtra(WALLET_ID, 0L)));
                    finish();
                } else
                    Toast.makeText(this, getString(R.string.err_operation_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
