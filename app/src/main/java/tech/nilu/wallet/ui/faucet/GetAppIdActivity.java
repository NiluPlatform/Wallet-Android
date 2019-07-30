package tech.nilu.wallet.ui.faucet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.util.UIUtils;

public class GetAppIdActivity extends BaseActivity implements HasSupportFragmentInjector {
    @Inject
    FaucetViewModel faucetViewModel;

    @BindView(R.id.tvAppId)
    TextView appIdText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_app_id);

        fetchAppID();
    }

    private void fetchAppID() {
        ProgressDialog progress = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        progress.show();

        faucetViewModel.getAppId().observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    appIdText.setText(response.getData().getAppId());
                else
                    Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
