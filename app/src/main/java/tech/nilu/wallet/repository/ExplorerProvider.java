package tech.nilu.wallet.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import tech.nilu.wallet.api.explorer.Ether1Explorer;
import tech.nilu.wallet.api.explorer.EthplorerExplorer;
import tech.nilu.wallet.api.explorer.Explorer;
import tech.nilu.wallet.api.explorer.NiluExplorer;
import tech.nilu.wallet.api.explorer.PirlExplorer;

@Singleton
public class ExplorerProvider {
    private final NetworkRepository networkRepository;
    private final EthplorerExplorer ethplorerExplorer;
    private final NiluExplorer niluExplorer;
    private final PirlExplorer pirlExplorer;
    private final Ether1Explorer ether1Explorer;

    @Inject
    public ExplorerProvider(NetworkRepository networkRepository, EthplorerExplorer ethplorerExplorer, NiluExplorer niluExplorer, PirlExplorer pirlExplorer, Ether1Explorer ether1Explorer) {
        this.networkRepository = networkRepository;
        this.ethplorerExplorer = ethplorerExplorer;
        this.niluExplorer = niluExplorer;
        this.pirlExplorer = pirlExplorer;
        this.ether1Explorer = ether1Explorer;
    }

    public Explorer create() {
        long chainId = networkRepository.getActiveNetwork().getChainId();
        if (chainId == 1)
            return ethplorerExplorer;
        else if (chainId == 512)
            return niluExplorer;
        else if (chainId == 3125659152L)
            return pirlExplorer;
        else if (chainId == 1313114)
            return ether1Explorer;
        return null;
    }
}
