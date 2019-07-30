package tech.nilu.wallet.injection;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import tech.nilu.wallet.BuildConfig;

/**
 * Created by root on 2/14/18.
 */

@Module
class NetworkModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //.addNetworkInterceptor(new StethoInterceptor())
                /*.certificatePinner(new CertificatePinner.Builder()
                        .add("walletapi.nilu.tech", "sha256/OOSMag8EXaFE5j4+xCVkb2a2/JUulZLkbJaCnJxXQIA=")
                        .add("walletapi.nilu.tech", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
                        .add("walletapi.nilu.tech", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
                        .build())*/
                .connectTimeout(2, TimeUnit.MINUTES)
                .proxy(Proxy.NO_PROXY)
                .readTimeout(2, TimeUnit.MINUTES);
        if (BuildConfig.DEBUG)
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .proxy(null);
        /*try {
            X509TrustManager trustManager = trustManagerForCertificates(context.getAssets().open("nilu.pem"));
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return builder
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return builder.build();
    }

    private X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty())
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");

        // Put the certificates a key store.
        KeyStore keyStore = newEmptyKeyStore();
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager))
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore() throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
