package io.github.devbobos.quicksell.helper.security;

import android.content.Context;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AES256 {
    private static AES256 instance;

    private AES256() { }

    public static AES256 getInstance() {
        if(instance == null) {
            instance = new AES256();
        }
        return instance;
    }

    public String encrypt(Context context, String data)
    {
        String result = "";
        try
        {
//            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            byte[] ivBytes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            final String key = getKeystoreAuthority(context);
            SecretKey secretKey = getSecretKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            result = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);

        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public String decrypt(Context context, String encryptedData)
    {
        String result = "";
        try
        {
//            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            byte[] ivBytes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            final String key = getKeystoreAuthority(context);
            SecretKey secretKey = getSecretKey(key);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.NO_WRAP));
            result = new String(decryptedBytes, "UTF-8");
        } catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private SecretKey getSecretKey(String key){
        SecretKey secretKey = null;
        KeystoreManager keystoreManager = new KeystoreManager();
        if(keystoreManager.hasKey(key)) {
            secretKey = keystoreManager.getSecretKey(key);
        }
        else {
            secretKey = keystoreManager.generateKey(key);
        }
        return secretKey;
    }

    private String getKeystoreAuthority(Context context){
        return context.getPackageName()+".keystore";
    }
}
