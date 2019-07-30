package tech.nilu.wallet.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.inject.Inject;
import javax.security.auth.x500.X500Principal;

/**
 * Created by root on 12/12/17.
 */

public class MyKeyStore {
    public static final String NILU_ALIAS;
    public static final String PASSWORD = "Password";
    public static final String IDENTIFIER = "Identifier";

    private static final int[] ALIAS = {99, 111, 46, 110, 105, 108, 105, 110, 46, 110, 105, 108, 117, 119, 97, 108, 108, 101, 116, 46, 75, 69, 89, 95, 83, 84, 79, 82, 69};

    static {
        StringBuilder sb = new StringBuilder();
        for (int i : ALIAS) sb.append((char) i);
        NILU_ALIAS = sb.toString();
    }

    private Context context;
    private KeyStore keyStore;

    @Inject
    public MyKeyStore(Context context) {
        this.context = context;
        try {
            this.keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void createNewKeys(final String alias) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 10);
                AlgorithmParameterSpec spec;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setCertificateSubject(new X500Principal("CN=Wallet, O=Nilu, C=MilkyWay"))
                            .setCertificateSerialNumber(BigInteger.ONE)
                            .setKeyValidityStart(start.getTime())
                            .setKeyValidityEnd(end.getTime())
                            .setDigests(KeyProperties.DIGEST_SHA256)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build();
                else
                    spec = new KeyPairGeneratorSpec.Builder(context)
                            .setAlias(alias)
                            .setSubject(new X500Principal("CN=Wallet, O=Nilu, C=MilkyWay"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.getTime())
                            .setEndDate(end.getTime())
                            .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);
                generator.generateKeyPair();
            }
        } catch (Exception e) {
            context.startActivity(new Intent("com.android.credentials.UNLOCK").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void deleteKey(final String alias) {
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String encryptString(final String alias, final String text) {
        try {
            PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();

            // Encrypt the text
            Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            input.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, input);
            cipherOutputStream.write(text.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] encrypted = outputStream.toByteArray();
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }

    public String decryptString(final String alias, String cipherText) {
        try {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);

            //Decrypt the text
            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1)
                values.add((byte) nextByte);

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++)
                bytes[i] = values.get(i);

            return new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }
}
