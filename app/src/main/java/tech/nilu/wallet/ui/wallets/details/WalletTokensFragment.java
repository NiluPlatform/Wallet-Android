package tech.nilu.wallet.ui.wallets.details;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.db.entity.ContractInfo;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.tokens.TokenDetailsActivity;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.BitmapUtils;

/**
 * Created by root on 3/3/18.
 */

public class WalletTokensFragment extends Fragment implements Injectable {
    private static final String WALLET_ID = "WalletId";

    @Inject
    WalletViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(android.R.id.list)
    RecyclerView list;

    private TokensAdapter adapter;

    public WalletTokensFragment() {

    }

    public static WalletTokensFragment newInstance(long walletId) {
        Bundle args = new Bundle();
        args.putLong(WALLET_ID, walletId);

        WalletTokensFragment fragment = new WalletTokensFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_tokens, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new TokensAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        list.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        list.setAdapter(adapter);
        loadTokens();
    }

    private void initUI() {
        swipeLayout.setEnabled(false);
        swipeLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void loadTokens() {
        swipeLayout.setRefreshing(true);
        viewModel.getContractInfosLive(getArguments().getLong(WALLET_ID)).observe(this, contractInfos -> {
            swipeLayout.setRefreshing(false);
            adapter.removeAll();

            for (ContractInfo ci : contractInfos) {
                adapter.add(adapter.getItemCount(), ci);
                viewModel.getERC20Balance(getArguments().getLong(WALLET_ID), ci).observe(this, balance -> {
                    if (balance != null && balance.getLiveStatus() == LiveResponseStatus.SUCCEED)
                        adapter.updateBalance(contractInfos.indexOf(ci), balance.getData());
                });
            }
        });
    }

    class TokensAdapter extends RecyclerView.Adapter {
        private Resources resources;
        private int holderIconSize;
        private List<ContractInfo> items;

        TokensAdapter(Context context) {
            this.items = new ArrayList<>();
            this.resources = context.getResources();
            this.holderIconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, resources.getDisplayMetrics());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TokenViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TokenViewHolder)
                ((TokenViewHolder) holder).bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        void add(int pos, ContractInfo item) {
            items.add(pos, item);
            notifyItemInserted(pos);
        }

        void removeAll() {
            items = new ArrayList<>();
            notifyDataSetChanged();
        }

        void updateBalance(int position, BigInteger balance) {
            if (position > -1) {
                items.get(position).setBalance(balance);
                notifyItemChanged(position);
            }
        }

        class TokenViewHolder extends RecyclerView.ViewHolder {
            @BindView(android.R.id.icon)
            ImageView icon;
            @BindView(R.id.tvName)
            TextView nameText;
            @BindView(R.id.tvAddress)
            TextView addressText;
            @BindView(R.id.tvBalance)
            TextView balanceText;

            private Context context;

            TokenViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_contract, parent, false));

                ButterKnife.bind(this, itemView);
                this.context = parent.getContext();
            }

            void bind(ContractInfo item) {
                Drawable placeholder = new BitmapDrawable(resources, BitmapUtils.getAvatar(holderIconSize, String.valueOf(item.getName().charAt(0)).toUpperCase()));
                Glide.with(itemView)
                        .load(item.getImage())
                        .apply(new RequestOptions()
                                .circleCrop()
                                .error(placeholder)
                                .placeholder(placeholder))
                        .into(icon);
                nameText.setText(item.getName());
                //holder.addressText.setText(String.format("0x%s", item.getAddress()));
                balanceText.setText(String.format("%s %s", Convert.fromWei(item.getBalance().toString(), Convert.Unit.ETHER).toString(), item.getSymbol() == null ? "Ξ" : item.getSymbol()));

                itemView.setOnClickListener(v -> context.startActivity(new Intent(context, TokenDetailsActivity.class)
                        .putExtra(TokenDetailsActivity.BALANCE, item.getBalance())
                        .putExtra(TokenDetailsActivity.WALLET_ID, item.getWalletId())
                        .putExtra(TokenDetailsActivity.CONTRACT_ID, item.getId())));
            }
        }
    }
}
