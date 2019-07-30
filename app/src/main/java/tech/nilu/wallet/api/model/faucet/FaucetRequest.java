package tech.nilu.wallet.api.model.faucet;

public class FaucetRequest {
    private final String identifier;
    private final String magic;

    public FaucetRequest(String identifier, String magic) {
        this.identifier = identifier;
        this.magic = magic;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getMagic() {
        return magic;
    }
}
