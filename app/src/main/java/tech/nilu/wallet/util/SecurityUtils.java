package tech.nilu.wallet.util;

import androidx.annotation.Nullable;

import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import tech.nilu.wallet.JniHandler;

public class SecurityUtils {
    private static final int[] KEY_CHARS = {
            112, 98, 106, 122, 109, 99, 119, 35, 66, 35, 73, 82, 97, 73, 57, 120,
            98, 106, 67, 113, 77, 54, 100, 101, 106, 118, 68, 100, 104, 112, 89, 98
    }; //pbjzmcw#B#IRaI9xbjCqM6dejvDdhpYb
    private static final int[] IV_CHARS = {
            107, 107, 119, 51, 90, 47, 66, 113, 117, 82, 80, 99, 98, 51, 49, 111, 104, 83, 71, 100, 119, 119, 61, 61
    }; //kkw3Z/BquRPcb31ohSGdww==
    private static final String IV;
    private static final String KEY;
    private static final String DEFAULT_HMAC = "HMACSHA256";

    static {
        StringBuilder ivBuilder = new StringBuilder();
        for (int i : IV_CHARS) ivBuilder.append((char) i);
        IV = ivBuilder.toString();

        StringBuilder keyBuilder = new StringBuilder();
        for (int i : KEY_CHARS) keyBuilder.append((char) i);
        KEY = keyBuilder.toString();
    }

    public static String createHmac(String message) {
        return createHmac(DEFAULT_HMAC, "1234567890123456789", message);
    }

    public static String createHmac(String algorithm, String secretKey, String message) {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), algorithm);
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);
            mac.update(message.getBytes());

            byte[] encrypted = mac.doFinal();
            return Hex.toHexString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static String encrypt(String plain) {
        byte[] key = KEY.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            AlgorithmParameterSpec iv = new IvParameterSpec(Base64.decode(IV));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(plain.getBytes());
            return Base64.toBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCode() {
        String board = JniHandler.getBuildBoard();
        String brand = JniHandler.getBuildBrand();
        String cpuABI = JniHandler.getBuildCpuABI();
        String device = JniHandler.getBuildDevice();
        String manufacturer = JniHandler.getBuildManufacturer();
        String model = JniHandler.getBuildModel();
        String product = JniHandler.getBuildProduct();
        String serial = JniHandler.getBuildSerial();

        String shortID = "35" +
                (board.length() % 10) +
                (brand.length() % 10) +
                (cpuABI.length() % 10) +
                (device.length() % 10) +
                (manufacturer.length() % 10) +
                (model.length() % 10) +
                (product.length() % 10);
        StringBuilder uuid = new StringBuilder(getUUID(shortID.hashCode(), serial.hashCode()));
        while (uuid.length() < 32)
            uuid.insert(0, 'f');
        return uuid.toString();
    }

    private static String getUUID(long mostSigBits, long leastSigBits) {
        return digits(mostSigBits >> 32, 8) +
                digits(mostSigBits >> 16, 4) +
                digits(mostSigBits, 4) +
                digits(leastSigBits >> 48, 4) +
                digits(leastSigBits, 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        String ss = Long.toHexString(hi | (val & (hi - 1)));
        return ss.substring(1);
    }
}
