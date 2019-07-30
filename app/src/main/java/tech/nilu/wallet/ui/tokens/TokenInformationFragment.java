package tech.nilu.wallet.ui.tokens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseViewModel;

/**
 * Created by root on 3/6/18.
 */

public class TokenInformationFragment extends Fragment implements Injectable {
    private static final String TOKEN_ID = "TokenId";

    @Inject
    TokenViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;
    @BindView(android.R.id.progress)
    ProgressBar progress;
    @BindView(R.id.contentLayout)
    View contentLayout;
    @BindView(R.id.tvName)
    TextView nameText;
    @BindView(R.id.tvSymbol)
    TextView symbolText;
    @BindView(R.id.tvTotalSupply)
    TextView totalSupplyText;
    @BindView(R.id.tvDecimals)
    TextView decimalsText;
    @BindView(R.id.tvRate)
    TextView rateText;
    @BindView(R.id.tvIsPayable)
    TextView isPayableText;

    private ContractInfo token;

    public TokenInformationFragment() {

    }

    public static TokenInformationFragment newInstance(long tokenId) {
        Bundle args = new Bundle();
        args.putLong(TOKEN_ID, tokenId);

        TokenInformationFragment fragment = new TokenInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_information, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        token = viewModel.getContractInfo(getArguments().getLong(TOKEN_ID));
        fetchTokenInformation();
    }

    private void fetchTokenInformation() {
        progress.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.INVISIBLE);
        viewModel.fetchERC20Contract(token.getWalletId(), token).observe(this, response -> {
            if (response.getLiveStatus() != LiveResponseStatus.IN_PROGRESS) {
                progress.setVisibility(View.GONE);

                if (response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    ContractInfo ci = response.getData();
                    contentLayout.setVisibility(View.VISIBLE);

                    nameText.setText(ci.getTokenName());
                    symbolText.setText(ci.getTokenSymbol());
                    totalSupplyText.setText(new BigDecimal(ci.getTokenTotalSupply()).divide(BigDecimal.TEN.pow(ci.getTokenDecimals().intValue())).toString());
                    decimalsText.setText(ci.getTokenDecimals().toString());
                    rateText.setText(String.format("%s %s", ci.getTokenRate().toString(), baseViewModel.getActiveNetwork().getSymbol()));
                    isPayableText.setText(ci.getTokenIsPayable() ? "Yes" : "No");
                }
            }
        });
    }
}
