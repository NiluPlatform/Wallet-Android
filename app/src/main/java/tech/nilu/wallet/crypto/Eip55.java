package tech.nilu.wallet.crypto;

import org.web3j.crypto.Hash;

public class Eip55 {
    public static String convertToEip55Address(String addr) {
        addr = addr.substring(2).toLowerCase();
        StringBuilder ret = new StringBuilder("0x");
        String hash = Hash.sha3String(addr).substring(2);
        for (int i = 0; i < addr.length(); i++) {
            String a = addr.charAt(i) + "";
            int h = Integer.parseInt(hash.charAt(i) + "", 16);
            if (h > 7) {
                ret.append(a.toUpperCase());
            } else
                ret.append(a);
        }
        return ret.toString();
    }

    public static boolean isValidAddress(String eip55) {
        String pattern = "(0x)?([0-9a-f]{40}|[0-9A-F]{40})";
        if (eip55.matches(pattern))
            return true;
        pattern = "(0x)?([0-9a-fA-F]{40})";
        if (!eip55.matches(pattern))
            return false;
        String addr = convertToEip55Address(eip55);
        return addr.equals(eip55);
    }
}
