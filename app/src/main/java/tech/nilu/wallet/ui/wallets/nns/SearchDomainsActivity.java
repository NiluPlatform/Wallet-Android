package tech.nilu.wallet.ui.wallets.nns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.abi.datatypes.Address;
import org.web3j.ens.EnsResolutionException;
import org.web3j.ens.NameHash;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseActivity;
import tech.nilu.wallet.ui.wallets.details.WalletDetailsActivity;
import tech.nilu.wallet.util.UIUtils;

public class SearchDomainsActivity extends BaseActivity implements HasSupportFragmentInjector {
    public static final String WALLET_ID = "WalletId";

    private static final int RC_REGISTER_DOMAIN = 1001;
    private static final int RC_RELEASE_DOMAIN = 1002;

    @Inject
    NNSViewModel nnsViewModel;
    @BindView(R.id.domainTextLayout)
    TextInputLayout domainTextLayout;
    @BindView(R.id.domainText)
    TextInputEditText domainText;
    @BindView(R.id.nodeText)
    TextView nodeText;
    @BindView(R.id.ownerText)
    TextView ownerText;
    @BindView(R.id.resolverText)
    TextView resolverText;
    @BindView(R.id.statusText)
    TextView statusText;
    @BindView(R.id.requestButton)
    Button requestButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_domains);

        initUI();

        domainText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryNNS();
                return true;
            }
            return false;
        });
        nnsViewModel.query.observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    Address owner = new Address(response.getData().getValue1());
                    Address resolver = new Address(response.getData().getValue2());
                    nodeText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s.me.nilu</font>", getString(R.string.domain), nnsViewModel.getDomain())));
                    ownerText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.owner), owner.equals(Address.DEFAULT) ? "-" : Eip55.convertToEip55Address(owner.toString()))));
                    resolverText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.resolver), resolver.equals(Address.DEFAULT) ? "-" : Eip55.convertToEip55Address(resolver.toString()))));
                    statusText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.status), owner.equals(Address.DEFAULT) ? "Available" : "Owned")));
                    requestButton.setEnabled(owner.equals(Address.DEFAULT));
                } else
                    Toast.makeText(this, response.toCustomException().getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        nnsViewModel.address.observe(this, response -> {
            if (response != null && response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                if (progressDialog.isShowing()) progressDialog.dismiss();

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED)
                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.already_own_domain, response.getData(), response.getData()))
                            .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                            .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                                dialog.dismiss();

                                startActivityForResult(new Intent(this, ReleaseDomainActivity.class)
                                        .putExtra(ReleaseDomainActivity.OWNED, response.getData())
                                        .putExtra(ReleaseDomainActivity.WALLET_ID, getIntent().getLongExtra(WALLET_ID, 0L)), RC_RELEASE_DOMAIN);
                            })
                            .show();
                else
                    startActivityForResult(new Intent(this, RegisterDomainActivity.class)
                            .putExtra(RegisterDomainActivity.SUBNODE, nnsViewModel.getDomain())
                            .putExtra(RegisterDomainActivity.WALLET_ID, getIntent().getLongExtra(WALLET_ID, 0L)), RC_REGISTER_DOMAIN);
            }
        });
    }

    @OnClick(R.id.searchButton)
    public void onSearchClick() {
        queryNNS();
    }

    @OnClick(R.id.requestButton)
    public void onRequestClick() {
        if (!progressDialog.isShowing()) progressDialog.show();
        nnsViewModel.setAddressData(getIntent().getLongExtra(WALLET_ID, 0L));
    }

    private void initUI() {
        progressDialog = UIUtils.getProgressDialog(this, false, getString(R.string.please_wait));
        nodeText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.domain), "-")));
        ownerText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.owner), "-")));
        resolverText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.resolver), "-")));
        statusText.setText(Html.fromHtml(String.format("%s: <font color=#808080>%s</font>", getString(R.string.status), "-")));
    }

    private void queryNNS() {
        requestButton.setEnabled(false);
        domainTextLayout.setError(null);

        String subnode = domainText.getText().toString();
        String node = subnode + ".me.nilu";
        if (subnode.length() < 3 || subnode.contains(".")) {
            domainTextLayout.setError(getString(R.string.err_invalid_domain));
            return;
        }
        try {
            String normalisedNode = NameHash.nameHash(node);
        } catch (EnsResolutionException e) {
            domainTextLayout.setError(getString(R.string.err_unnormal_domain));
            return;
        }

        UIUtils.hideKeyboard(this, getCurrentFocus());
        if (!progressDialog.isShowing()) progressDialog.show();
        nnsViewModel.setQueryData(getIntent().getLongExtra(WALLET_ID, 0L), node);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_REGISTER_DOMAIN && resultCode == RESULT_OK && data != null) {
            setResult(RESULT_OK, new Intent().putExtra(WalletDetailsActivity.DOMAIN, data.getStringExtra(WalletDetailsActivity.DOMAIN)));
            finish();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
