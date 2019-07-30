package tech.nilu.wallet.repository;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;

/**
 * Created by root on 2/14/18.
 */

@Singleton
public class Web3jProvider {
    private final OkHttpClient httpClient;
    private final NetworkRepository networkRepository;

    @Inject
    public Web3jProvider(OkHttpClient httpClient, NetworkRepository networkRepository) {
        this.httpClient = httpClient;
        this.networkRepository = networkRepository;
    }

    public Web3j create() {
        return Web3jFactory.build(new HttpService(networkRepository.getActiveNetwork().getAddress(), httpClient, false));
    }
}
