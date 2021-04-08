package io.github.devbobos.quicksell.helper.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeystoreManager
{
    private static final String PROVIDER_ANDROID_KEY_STORE = "AndroidKeyStore";

    public boolean hasKey(String key)
    {
        boolean isOk = false;
        try
        {
            KeyStore keystore = null;
            keystore = KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE);
            keystore.load(null);
            isOk = keystore.isKeyEntry(key);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        CLog.e("hasKey "+isOk);
        return isOk;
    }

    public boolean isNull(String key)
    {
        return !hasKey(key);
    }

    public void deleteKey(String key)
    {
        KeyStore keystore = null;
        try {
            keystore = KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE);
            keystore.load(null);
            keystore.deleteEntry(key);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SecretKey generateKey(String key)
    {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", PROVIDER_ANDROID_KEY_STORE);

            int purposes = KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT;
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(key, purposes)
                    .setKeySize(256)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setRandomizedEncryptionRequired(false)
                    .build();
            keyGenerator.init(keyGenParameterSpec);
            secretKey = keyGenerator.generateKey();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public SecretKey getSecretKey(String key)
    {
        SecretKey result = null;
        try {
            KeyStore keystore = KeyStore.getInstance(PROVIDER_ANDROID_KEY_STORE);
            keystore.load(null);
            result = (SecretKey) keystore.getKey(key, null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return result;
    }
}