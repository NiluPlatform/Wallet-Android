package tech.nilu.wallet.ui.tokens.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.model.GasParams;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.WalletBaseRepository;
import tech.nilu.wallet.repository.contracts.NiluToken;
import tech.nilu.wallet.ui.common.AVLIVDialogFragment;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.password.WalletPasswordDialogFragment;
import tech.nilu.wallet.ui.tokens.TokenViewModel;
import tech.nilu.wallet.ui.wallets.WalletViewModel;

public class DeployActivity extends BaseActivity implements HasSupportFragmentInjector, WalletPasswordDialogFragment.WalletPasswordClickListener {
    public static final String WALLET_ID = "WalletId";
    public static final String NAME = "Name";
    public static final String SYMBOL = "Symbol";
    public static final String DECIMALS = "Decimals";
    public static final String TOTAL_SUPPLY = "TotalSupply";

    @Inject
    WalletViewModel walletViewModel;
    @Inject
    TokenViewModel tokenViewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @BindView(android.R.id.progress)
    ProgressBar progress;
    @BindView(R.id.deployInfoLayout)
    View deployInfoLayout;
    @BindView(R.id.creatorText)
    TextView creatorText;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.symbolText)
    TextView symbolText;
    @BindView(R.id.decimalsText)
    TextView decimalsText;
    @BindView(R.id.totalSupplyText)
    TextView totalSupplyText;
    @BindView(R.id.feeText)
    TextView feeText;
    @BindView(R.id.dataText)
    EditText dataText;

    private GasParams params = null;
    private WalletBaseRepository.WalletExtensionData walletData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy);

        walletData = walletViewModel.getWalletData(getIntent().getLongExtra(WALLET_ID, 0L));
        initUI();
        getDeploymentFee();
    }

    @OnClick(R.id.deployButton)
    public void onDeployClick() {
        WalletPasswordDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    private void initUI() {
        String creator = walletData.getCredentials().getAddress();
        String name = getIntent().getStringExtra(NAME);
        String symbol = getIntent().getStringExtra(SYMBOL);
        String decimals = getIntent().getStringExtra(DECIMALS);
        String totalSupply = getIntent().getStringExtra(TOTAL_SUPPLY);
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList(new Utf8String(name),
                new Utf8String(symbol),
                new Uint8(new BigInteger(decimals)),
                new Uint256(new BigDecimal(totalSupply).multiply(BigDecimal.TEN.pow(Integer.parseInt(decimals))).toBigInteger())));
        String data = NiluToken.getBinary() + encodedConstructor;

        creatorText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.creator), Eip55.convertToEip55Address(creator))));
        nameText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.name), name)));
        symbolText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.symbol), symbol)));
        decimalsText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.decimals), decimals)));
        totalSupplyText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.total_supply), totalSupply)));
        dataText.setText(data);
        dataText.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
            }
            return false;
        });
    }

    private void getDeploymentFee() {
        String name = getIntent().getStringExtra(NAME);
        String symbol = getIntent().getStringExtra(SYMBOL);
        String decimals = getIntent().getStringExtra(DECIMALS);
        String totalSupply = getIntent().getStringExtra(TOTAL_SUPPLY);

        progress.setVisibility(View.VISIBLE);
        deployInfoLayout.setVisibility(View.INVISIBLE);
        tokenViewModel.getDeploymentFee(
                walletData.getCredentials(),
                name,
                symbol,
                new BigInteger(decimals),
                new BigDecimal(totalSupply).multiply(BigDecimal.TEN.pow(Integer.parseInt(decimals))).toBigInteger()
        ).observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.setVisibility(View.GONE);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    deployInfoLayout.setVisibility(View.VISIBLE);

                    params = response.getData();
                    BigInteger fee = params.getGasPrice().multiply(params.getGasLimit());
                    feeText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s %s</font>", getString(R.string.fee), Convert.fromWei(fee.toString(), Convert.Unit.ETHER).toString(), walletViewModel.getNetwork(walletData.getWallet().getNetworkId()).getSymbol())));
                } else {
                    Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onAccept(WalletPasswordDialogFragment dialog, String password) {
        dialog.dismiss();

        String name = getIntent().getStringExtra(NAME);
        String symbol = getIntent().getStringExtra(SYMBOL);
        String decimals = getIntent().getStringExtra(DECIMALS);
        String totalSupply = getIntent().getStringExtra(TOTAL_SUPPLY);

        AVLIVDialogFragment progress = AVLIVDialogFragment.newInstance(getString(R.string.deploying, name));
        progress.setCancelable(false);
        progress.show(getSupportFragmentManager(), null);
        tokenViewModel.deployContract(
                walletData.getCredentials(),
                params.getGasPrice(),
                params.getGasLimit(),
                name,
                symbol,
                new BigInteger(decimals),
                new BigDecimal(totalSupply).multiply(BigDecimal.TEN.pow(Integer.parseInt(decimals))).toBigInteger()
        ).observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    NiluToken token = response.getData();
                    startActivity(new Intent(this, TokenReceiptActivity.class)
                            .putExtra(TokenReceiptActivity.CONTRACT_ADDRESS, token.getContractAddress())
                            .putExtra(TokenReceiptActivity.HASH, token.getTransactionReceipt().getTransactionHash())
                            .putExtra(TokenReceiptActivity.BLOCK_NUMBER, token.getTransactionReceipt().getBlockNumber().toString())
                            .putExtra(TokenReceiptActivity.CREATOR, token.getTransactionReceipt().getFrom())
                            .putExtra(TokenReceiptActivity.GAS_USED, token.getTransactionReceipt().getGasUsed().toString()));
                    setResult(RESULT_OK);
                    finish();
                } else
                    Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
