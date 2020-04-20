package tech.nilu.wallet.ui.wallets.importing;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.MyStatePagerAdapter;

/**
 * Created by root on 2/7/18.
 */

public class ImportWalletActivity extends BaseActivity implements HasSupportFragmentInjector {
    public static final String NETWORK = "Network";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);

        initTabs();
    }

    private void initTabs() {
        MyStatePagerAdapter adapter = new MyStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MnemonicsImportFragment.newInstance(getIntent().getLongExtra(NETWORK, 0L)), getString(R.string.mnemonics));
        adapter.addFragment(KeystoreImportFragment.newInstance(getIntent().getLongExtra(NETWORK, 0)), getString(R.string.keystore));
        adapter.addFragment(PrivateKeyImportFragment.newInstance(getIntent().getLongExtra(NETWORK, 0)), getString(R.string.private_key));
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
