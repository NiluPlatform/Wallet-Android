package tech.nilu.wallet.ui.receive;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;

import net.glxn.qrgen.android.QRCode;

import org.web3j.crypto.CipherException;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.crypto.Eip55;
import tech.nilu.wallet.db.entity.Network;
import tech.nilu.wallet.db.entity.Wallet;
import tech.nilu.wallet.injection.Injectable;
import tech.nilu.wallet.ui.common.BaseViewModel;
import tech.nilu.wallet.ui.common.NetworkListener;
import tech.nilu.wallet.ui.wallets.WalletViewModel;
import tech.nilu.wallet.util.MyKeyStore;

/**
 * Created by Navid on 12/14/17.
 */

public class ReceiveMoneyFragment extends Fragment implements Injectable, NetworkListener {
    private static final String NETWORK = "Network";

    @Inject
    WalletViewModel viewModel;
    @Inject
    BaseViewModel baseViewModel;

    @BindView(R.id.walletsSpinner)
    Spinner walletsSpinner;
    @BindView(R.id.qrSwitcher)
    ImageSwitcher qrSwitcher;

    private Network network;
    private ArrayAdapter<Wallet> adapter;

    public ReceiveMoneyFragment() {

    }

    public static ReceiveMoneyFragment newInstance(Network network) {
        Bundle args = new Bundle();
        args.putSerializable(NETWORK, network);

        ReceiveMoneyFragment fragment = new ReceiveMoneyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NETWORK, network);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive_money, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null)
            network = (Network) getArguments().getSerializable(NETWORK);
        else
            network = (Network) savedInstanceState.getSerializable(NETWORK);
        loadWallets(network);
        walletsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wallet wallet = adapter.getItem(position);
                File parentDir = getActivity().getFilesDir();
                String password = baseViewModel.retrieveString(MyKeyStore.PASSWORD);
                try {
                    generateQR(Eip55.convertToEip55Address(viewModel.getWalletData(wallet, parentDir, password).getCredentials().getAddress()));
                } catch (IOException | CipherException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initUI() {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        walletsSpinner.setAdapter(adapter);

        qrSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        qrSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        qrSwitcher.setFactory(() -> {
            ImageView image = new ImageView(getActivity());
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setLayoutParams(new ImageSwitcher.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return image;
        });
    }

    public void loadWallets(Network selectedNetwork) {
        viewModel.getWalletsLive(selectedNetwork.getId()).observe(this, wallets -> {
            adapter.clear();
            adapter.addAll(wallets);
        });
    }

    private void generateQR(String address) {
        String qrCode = Uri.parse("http://nilu.tech/" + address).buildUpon().build().toString();
        qrSwitcher.setImageDrawable(new BitmapDrawable(getResources(),
                QRCode.from(qrCode)
                        .withSize(300, 300)
                        .withCharset("UTF-8")
                        .bitmap()
        ));
    }

    @Override
    public void onNetworkChanged(Network selectedNetwork) {
        network = selectedNetwork;
        if (getView() != null) loadWallets(network);
    }
}
