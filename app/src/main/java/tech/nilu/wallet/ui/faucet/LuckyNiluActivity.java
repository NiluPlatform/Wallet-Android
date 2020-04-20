package tech.nilu.wallet.ui.faucet;

import android.app.ProgressDialog;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.web3j.crypto.CipherException;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.MyKeyStore;
import tech.nilu.wallet.util.ShakeDetector;
import tech.nilu.wallet.util.UIUtils;

public class LuckyNiluActivity extends BaseActivity implements HasSupportFragmentInjector, ShakeDetector.Listener {
    @Inject
    BaseViewModel baseViewModel;
    @Inject
    FaucetViewModel faucetViewModel;
    @Inject
    WalletViewModel walletViewModel;

    @BindView(R.id.walletsSpinner)
    Spinner walletsSpinner;
    @BindView(android.R.id.progress)
    ArcProgress progress;
    @BindView(R.id.tvShakePrompt)
    TextView promptText;

    private ShakeDetector shakeDetector;
    private Random random = new Random();
    private ArrayAdapter<Wallet> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_nilu);

        shakeDetector = new ShakeDetector(this);
        shakeDetector.start((SensorManager) getSystemService(SENSOR_SERVICE));

        initUI();
        walletViewModel.getWalletsLive(faucetViewModel.getNetwork(getString(R.string.nilu_rpc)).getId()).observe(this, wallets -> {
            adapter.clear();
            adapter.addAll(wallets);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shakeDetector.stop();
    }

    private void initUI() {
        promptText.setText(Html.fromHtml(String.format("<b>%s</b><br>%s", getString(R.string.shake_prompt), getString(R.string.shake_prompt_2))));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        walletsSpinner.setAdapter(adapter);
    }

    @Override
    public void hearShake() {
        int newProgress = progress.getProgress() + (random.nextInt(11) + 10);
        if (newProgress > 100) {
            newProgress = 100;
            shakeDetector.stop();

            getFaucet();
        }
        progress.setProgress(newProgress);
    }

    private void getFaucet() {
        if (walletsSpinner.getSelectedItemPosition() == -1) {
            Toast.makeText(this, getString(R.string.err_no_wallet_specified), Toast.LENGTH_SHORT).show();
            return;
        }
        String address = getSelectedWalletAddress(walletsSpinner.getSelectedItemPosition());
        if (address == null) {
            Toast.makeText(this, getString(R.string.err_operation_failed), Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progress = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        progress.show();

        faucetViewModel.getFaucet("NILU", address).observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    Toast.makeText(this, getString(R.string.success_won_lucky_nilu, Convert.fromWei(response.getData().getValue().toString(), Convert.Unit.ETHER).toString()), Toast.LENGTH_LONG).show();
                    finish();
                } else
                    Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSelectedWalletAddress(int position) {
        Wallet wallet = adapter.getItem(position);
        String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);
        try {
            return walletViewModel.getWalletData(wallet, getFilesDir(), password).getCredentials().getAddress();
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
