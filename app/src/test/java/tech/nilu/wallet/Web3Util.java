package tech.nilu.wallet;


import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Web3Util {
    private static final String MAINNET = "https://walletapi.nilu.tech";
    private static final String TESTNET = "http://192.168.2.202:7545";

    public static Web3j getWeb3j(boolean prd) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        return Web3jFactory.build(new HttpService(prd ? MAINNET : TESTNET, httpClient, true));
    }

    public static Credentials getCredential(boolean prd) {
        try {
            return prd ?
                    Credentials.create("258a1b50a92d08c3de4dc3f8eb484508f6b3e555db06a1dd2900631f484b6bdb") :
                    //WalletUtils.loadCredentials("andromeda", "/root/IdeaProjects/NiluNameServer/laptop-gilu") :
                    Credentials.create("da52430e6efb48156ebbb8fa41dc3d31b08f7495d093ecac0f02910d48259dad");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
