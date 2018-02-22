package me.opens.password_manager.service;

import org.apache.commons.codec.binary.Base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import static me.opens.password_manager.util.Constants.DESEDE_ENCRYPTION_SCHEME;
import static me.opens.password_manager.util.Constants.KEY_PREFIX;
import static me.opens.password_manager.util.Constants.KEY_SUFFIX;
import static me.opens.password_manager.util.Constants.UNICODE_FORMAT;

public class EncryptionService {

    private String encryptionScheme;
    private Cipher cipher;
    SecretKey key;
    private SecretKeyFactory skf;
    private KeySpec ks;
    private final String encryptionKey;
    private final byte[] arrayBytes;

    public EncryptionService(String key) throws Exception {
        encryptionKey = String.format("%s%s%s", KEY_PREFIX, key, KEY_SUFFIX);
        arrayBytes = encryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        encryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        skf = SecretKeyFactory.getInstance(encryptionScheme);
        cipher = Cipher.getInstance(encryptionScheme);
        this.key = skf.generateSecret(ks);
    }

    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}
