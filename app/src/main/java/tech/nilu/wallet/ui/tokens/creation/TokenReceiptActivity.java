package tech.nilu.wallet.ui.tokens.creation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.send.SendMoneyActivity;

public class TokenReceiptActivity extends BaseActivity {
    public static final String CONTRACT_ADDRESS = "ContractAddress";
    public static final String HASH = "Hash";
    public static final String BLOCK_NUMBER = "BlockNumber";
    public static final String CREATOR = "Creator";
    public static final String GAS_USED = "GasUsed";

    @BindView(R.id.tvContractAddress)
    TextView contractAddressText;
    @BindView(R.id.tvHash)
    TextView hashText;
    @BindView(R.id.tvBlockNumber)
    TextView blockNumberText;
    @BindView(R.id.tvCreator)
    TextView creatorText;
    @BindView(R.id.tvGasUsed)
    TextView gasUsedText;
    @BindView(R.id.tvDeploymentPrompt)
    TextView promptText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_receipt);

        initUI();
    }

    @OnClick(R.id.tvHash)
    public void onHashClick() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://explorer.nilu.tech/#/transaction/" + getIntent().getStringExtra(HASH))));
    }

    @OnClick(R.id.tvDeploymentPrompt)
    public void onPromptClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.app_name), Eip55.convertToEip55Address(getIntent().getStringExtra(CONTRACT_ADDRESS)));
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Token's address copied", Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        promptText.setText(Html.fromHtml(String.format("%s (<font color=#1976d2><u>%s</u></font>). %s%n<font color=#F44336>%s</font>", getString(R.string.token_deployment_prompt_1), getIntent().getStringExtra(CONTRACT_ADDRESS), getString(R.string.token_deployment_prompt_2), getString(R.string.token_deployment_caution))));
        contractAddressText.setText(getIntent().getStringExtra(CONTRACT_ADDRESS));
        hashText.setText(getIntent().getStringExtra(HASH));
        hashText.setPaintFlags(hashText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        blockNumberText.setText(getIntent().getStringExtra(BLOCK_NUMBER));
        creatorText.setText(Eip55.convertToEip55Address(getIntent().getStringExtra(CREATOR)));
        gasUsedText.setText(getIntent().getStringExtra(GAS_USED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_token_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_activate:
                Uri data = Uri.parse("http://nilu.tech/" + getIntent().getStringExtra(CONTRACT_ADDRESS))
                        .buildUpon()
                        .appendQueryParameter("amount", "100")
                        .appendQueryParameter("network", "1")
                        .appendQueryParameter("creator", getIntent().getStringExtra(CREATOR))
                        .build();
                startActivity(new Intent(this, SendMoneyActivity.class).setData(data));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
