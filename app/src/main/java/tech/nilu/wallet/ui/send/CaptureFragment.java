package tech.nilu.wallet.ui.send;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.DecodeFormatManager;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.nilu.wallet.R;
import tech.nilu.wallet.ui.send.transfer.TransferActivity;

/**
 * Created by root on 1/23/18.
 */

public class CaptureFragment extends Fragment {
    private static final int RC_TRANSFER = 1001;

    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView barcodeScannerView;

    private BeepManager beepManager;

    public CaptureFragment() {

    }

    public static CaptureFragment newInstance(ArrayList<String> decodeFormats) {
        Bundle args = new Bundle();
        args.putStringArrayList(Intents.Scan.FORMATS, decodeFormats);

        CaptureFragment fragment = new CaptureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        beepManager = new BeepManager(getActivity());
        barcodeScannerView.setStatusText("");
        barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(DecodeFormatManager.parseDecodeFormats(getFormatsIntent()), null, null, 0));
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() == null) return;
                barcodeScannerView.pause();
                beepManager.playBeepSoundAndVibrate();
                String scanned = result.getText();
                switch (result.getBarcodeFormat()) {
                    case QR_CODE:
                        if (scanned.trim().startsWith("http://nilu.tech/")) {
                            Uri uri = Uri.parse(scanned);
                            String address = uri.getPath().substring(1);
                            String amount = uri.getQueryParameter("amount");
                            String token = uri.getQueryParameter("token");
                            String data = uri.getQueryParameter("data");
                            Intent intent = new Intent()
                                    .putExtra(TransferActivity.ADDRESS, address)
                                    .putExtra(TransferActivity.AMOUNT, amount)
                                    .putExtra(TransferActivity.TOKEN, token)
                                    .putExtra(TransferActivity.DATA, data);
                            getActivity().setResult(AppCompatActivity.RESULT_OK, intent);
                            getActivity().finish();
                        }
                        break;
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    private Intent getFormatsIntent() {
        Intent result = new Intent();
        if (getArguments() != null) {
            ArrayList<String> desiredBarcodeFormats = getArguments().getStringArrayList(Intents.Scan.FORMATS);
            if (desiredBarcodeFormats != null) {
                StringBuilder joinedByComma = new StringBuilder();
                for (String format : desiredBarcodeFormats) {
                    if (joinedByComma.length() > 0) {
                        joinedByComma.append(',');
                    }
                    joinedByComma.append(format);
                }
                result.putExtra(Intents.Scan.FORMATS, joinedByComma.toString());
            }
        }
        return result;
    }
}
