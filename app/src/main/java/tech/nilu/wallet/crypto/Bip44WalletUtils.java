package tech.nilu.wallet.crypto;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class Bip44WalletUtils {
    public static Bip44Wallet generateBip44Wallet(Bip39Locale lang, String password, File destinationDirectory) throws CipherException, IOException {
        Bip44Wallet wallet = new Bip44Wallet(lang, null);
        Bip44Wallet.HDKey key = wallet.createKey("M/44H/60H/0H/0/0");

        ECKeyPair privateKey = ECKeyPair.create(key.priv);
        String walletFile = WalletUtils.generateWalletFile(password, privateKey, destinationDirectory, false);
        wallet.setFilename(walletFile);

        return wallet;
    }

    public static Credentials loadBip44Credentials(String mnemonic) {
        Bip44Wallet wallet = new Bip44Wallet("", mnemonic);
        Bip44Wallet.HDKey dk = wallet.createKey("M/44H/60H/0H/0/0");
        return Credentials.create(dk.priv.toString(16));
    }
}
