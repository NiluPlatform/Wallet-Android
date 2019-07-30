package tech.nilu.wallet.ui.wallets.backup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.ui.common.BaseActivity;

/**
 * Created by root on 2/6/18.
 */

public class BackupMnemonicsActivity extends BaseActivity {
    public static final String MNEMONIC = "Mnemonic";

    private static final int RC_CONFIRM_MNEMONIC = 1001;

    @BindView(R.id.mnemonicWordsText)
    TextView mnemonicWordsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_mnemonics);
        ButterKnife.bind(this);

        mnemonicWordsText.setText(getIntent().getStringExtra(MNEMONIC));
    }

    @OnClick(R.id.nextButton)
    public void onNextClick() {
        String mnemonic = getIntent().getStringExtra(MNEMONIC);
        startActivityForResult(new Intent(this, ConfirmMnemonicsActivity.class)
                .putExtra(ConfirmMnemonicsActivity.MNEMONIC, mnemonic), RC_CONFIRM_MNEMONIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CONFIRM_MNEMONIC && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
