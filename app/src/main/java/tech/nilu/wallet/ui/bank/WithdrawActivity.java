package tech.nilu.wallet.ui.bank;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.ui.common.BaseActivity;

public class WithdrawActivity extends BaseActivity implements HasSupportFragmentInjector {
    @BindView(android.R.id.progress)
    ProgressBar progress;
    @BindView(R.id.withdrawInfoLayout)
    View withdrawInfoLayout;
    @BindView(R.id.feeText)
    TextView feeText;
    @BindView(R.id.toText)
    TextView toText;
    @BindView(R.id.amountText)
    TextView amountText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);


    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
