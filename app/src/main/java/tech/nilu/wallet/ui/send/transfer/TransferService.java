package tech.nilu.wallet.ui.send.transfer;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import tech.nilu.wallet.R;
import tech.nilu.wallet.model.LiveResponseStatus;
import tech.nilu.wallet.repository.TransactionRepository;

/**
 * Created by root on 1/29/18.
 */

public class TransferService extends IntentService {
    public static final String HASH = "Hash";
    public static final String AMOUNT = "Amount";
    public static final String NETWORK_SYMBOL = "NetworkSymbol";

    @Inject
    TransactionRepository transactionRepository;

    public TransferService() {
        super("Nilu Transfer Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String hash = intent.getStringExtra(HASH);
        String amount = intent.getStringExtra(AMOUNT);
        String networkSymbol = intent.getStringExtra(NETWORK_SYMBOL);

        try {
            transactionRepository.getTransactionReceipt(hash).observeForever(response -> {
                if (response != null && response.getLiveStatus() == LiveResponseStatus.SUCCEED) {
                    NotificationCompat.Builder builder;
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        manager.createNotificationChannel(new NotificationChannel(getString(R.string.app_name), "Wallet", NotificationManager.IMPORTANCE_DEFAULT));
                        builder = new NotificationCompat.Builder(this, getString(R.string.app_name));
                    } else
                        builder = new NotificationCompat.Builder(this);
                    Notification notification = builder.setAutoCancel(true)
                            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                            .setContentText(String.format("%s %s transferred.", amount, networkSymbol))
                            .setContentTitle("Transfer Done!")
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                            .setShowWhen(true)
                            .setSmallIcon(R.drawable.ic_stat_successful_transaction)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("%s %s transferred from %s to %s successfully.", amount, networkSymbol, response.getData().getFrom(), response.getData().getTo())))
                            .setTicker("Transfer Done!")
                            .build();
                    manager.notify(1001, notification);
                }
            });
        } catch (IOException | TransactionException e) {
            e.printStackTrace();
        }
    }
}
