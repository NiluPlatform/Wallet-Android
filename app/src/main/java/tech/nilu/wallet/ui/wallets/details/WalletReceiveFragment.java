package tech.nilu.wallet.ui.wallets.details;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.textfield.TextInputEditText;

import net.glxn.qrgen.android.QRCode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.ui.wallets.WalletViewModel;

/**
 * Created by root on 1/13/18.
 */

public class WalletReceiveFragment extends Fragment implements Injectable {
    private static final String WALLET_ID = "WalletId";
    private static final String WALLET_ADDRESS = "WalletAddress";

    @Inject
    WalletViewModel viewModel;
    @BindView(R.id.qrSwitcher)
    ImageSwitcher qrSwitcher;
    @BindView(R.id.amountText)
    TextInputEditText amountText;

    private Wallet wallet;
    private String qrCode;

    public WalletReceiveFragment() {

    }

    public static WalletReceiveFragment newInstance(long id, String address) {
        Bundle args = new Bundle();
        args.putLong(WALLET_ID, id);
        args.putString(WALLET_ADDRESS, address);

        WalletReceiveFragment fragment = new WalletReceiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_receive, container, false);
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

        wallet = viewModel.getWallet(getArguments().getLong(WALLET_ID));
        generateQR(Eip55.convertToEip55Address(getArguments().getString(WALLET_ADDRESS)),
                amountText.getText().toString(),
                "ETH");
        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                generateQR(getArguments().getString(WALLET_ADDRESS),
                        s.toString(),
                        "ETH");
            }
        });
    }

    @OnClick(R.id.btnShare)
    public void onShareClick() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, String.format("My wallet's address is:%n%s%n%nUse Nilu & send %s on %s to me with this link:%n%s", Eip55.convertToEip55Address(getArguments().getString(WALLET_ADDRESS)), "ETH", viewModel.getNetwork(wallet.getNetworkId()).getName(), qrCode));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void initUI() {
        qrSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        qrSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        qrSwitcher.setFactory(() -> {
            ImageView image = new ImageView(getActivity());
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setLayoutParams(new ImageSwitcher.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return image;
        });
    }

    private void generateQR(String address, String amount, String token) {
        qrCode = Uri.parse("http://nilu.tech/" + address)
                .buildUpon()
                .appendQueryParameter("amount", amount)
                .appendQueryParameter("token", token)
                .build().toString();
        qrSwitcher.setImageDrawable(new BitmapDrawable(getResources(),
                QRCode.from(qrCode)
                        .withSize(280, 280)
                        .withCharset("UTF-8")
                        .bitmap()
        ));
    }
}
