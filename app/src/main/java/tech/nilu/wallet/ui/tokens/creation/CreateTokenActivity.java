package tech.nilu.wallet.ui.tokens.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.wallets.WalletViewModel;

public class CreateTokenActivity extends BaseActivity implements HasSupportFragmentInjector {
    private static final int RC_DEPLOY = 1001;

    @Inject
    BaseViewModel baseViewModel;
    @Inject
    WalletViewModel walletViewModel;

    @BindView(R.id.walletsSpinner)
    Spinner walletsSpinner;
    @BindView(R.id.nameTextLayout)
    TextInputLayout nameTextLayout;
    @BindView(R.id.nameText)
    TextInputEditText nameText;
    @BindView(R.id.symbolTextLayout)
    TextInputLayout symbolTextLayout;
    @BindView(R.id.symbolText)
    TextInputEditText symbolText;
    @BindView(R.id.decimalsTextLayout)
    TextInputLayout decimalsTextLayout;
    @BindView(R.id.decimalsText)
    TextInputEditText decimalsText;
    @BindView(R.id.supplyTextLayout)
    TextInputLayout supplyTextLayout;
    @BindView(R.id.supplyText)
    TextInputEditText supplyText;

    private ArrayAdapter<Wallet> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_erc20);

        initUI();
        walletViewModel.getWalletsLive(baseViewModel.getActiveNetwork().getId()).observe(this, wallets -> {
            adapter.clear();
            adapter.addAll(wallets);
        });
    }

    @OnClick(R.id.createButton)
    public void onCreateClick() {
        nameTextLayout.setError(null);
        symbolTextLayout.setError(null);
        decimalsTextLayout.setError(null);
        supplyTextLayout.setError(null);
        if (walletsSpinner.getSelectedItemPosition() == -1) {
            Toast.makeText(this, getString(R.string.err_no_wallet_specified), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nameText.getText().toString())) {
            nameTextLayout.setError(getString(R.string.err_empty_name));
            return;
        }
        if (TextUtils.isEmpty(symbolText.getText().toString())) {
            symbolTextLayout.setError(getString(R.string.err_empty_symbol));
            return;
        }
        if (TextUtils.isEmpty(decimalsText.getText().toString())) {
            decimalsTextLayout.setError(getString(R.string.err_empty_decimals));
            return;
        }
        if (TextUtils.isEmpty(supplyText.getText().toString())) {
            supplyTextLayout.setError(getString(R.string.err_empty_supply));
            return;
        }

        long walletId = adapter.getItem(walletsSpinner.getSelectedItemPosition()).getId();
        String name = nameText.getText().toString();
        String symbol = symbolText.getText().toString();
        String decimals = decimalsText.getText().toString();
        String totalSupply = supplyText.getText().toString();

        try {
            int decimalsInt = Integer.parseInt(decimals);
            if (decimalsInt < 0 || decimalsInt > 18) {
                decimalsTextLayout.setError(getString(R.string.err_invalid_decimals));
                return;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            decimalsTextLayout.setError(getString(R.string.err_invalid_decimals));
            return;
        }
        try {
            int totalSupplyInt = Integer.parseInt(totalSupply);
            if (totalSupplyInt < 1) {
                supplyTextLayout.setError(getString(R.string.err_invalid_supply));
                return;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            supplyTextLayout.setError(getString(R.string.err_invalid_supply));
            return;
        }

        startActivityForResult(new Intent(this, DeployActivity.class)
                .putExtra(DeployActivity.WALLET_ID, walletId)
                .putExtra(DeployActivity.NAME, name)
                .putExtra(DeployActivity.SYMBOL, symbol)
                .putExtra(DeployActivity.DECIMALS, decimals)
                .putExtra(DeployActivity.TOTAL_SUPPLY, totalSupply), RC_DEPLOY);
    }

    private void initUI() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        walletsSpinner.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_DEPLOY && resultCode == RESULT_OK)
            finish();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
