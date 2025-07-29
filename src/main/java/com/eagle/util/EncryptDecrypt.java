package com.eagle.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public class EncryptDecrypt {

    private static final String ALGORITHM = "AES";
    private static final String MODE_PADDING = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE_BYTES = 16;
    private static final int KEY_SIZE_BYTES = 16;

    private final SecretKeySpec secretKey;

    @Value("${security.jwt.secretLocalKey}")
    private String secret;

    public EncryptDecrypt(@Value("${security.jwt.secretLocalKey}") String secret) throws IllegalAccessException {
        if(secret == null || secret.isEmpty()){
            throw new IllegalAccessException("Secret key not found");
        }
        this.secretKey = createSecretKey(secret);
    }


    private SecretKeySpec createSecretKey(String secret){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            byte[] keyBytes128 = Arrays.copyOf(keyBytes, KEY_SIZE_BYTES);
            return new SecretKeySpec(keyBytes128, ALGORITHM);
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error initializing secret key :" ,e);
        }
    }

    /* Encrypts the plaintext string.
     * @param plaintext The plaintext string to encrypt.
     * @return The Base64 encoded encrypted string.
     */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("Plaintext cannot be null or empty");
        }
        try {

            // Generate a random IV
            byte[] iv = generateRandomIV();
            Cipher cipher = Cipher.getInstance(MODE_PADDING);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            // Encrypt the plaintext
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            // Combine IV and encrypted data
            byte[] ivAndEncrypted = new byte[IV_SIZE_BYTES + encrypted.length];
            System.arraycopy(iv, 0, ivAndEncrypted, 0, IV_SIZE_BYTES);
            System.arraycopy(encrypted, 0, ivAndEncrypted, IV_SIZE_BYTES, encrypted.length);
            // Return Base64 encoded string
            return Base64.getEncoder().encodeToString(ivAndEncrypted);
        } catch (Exception e) {
            // e.printStackTrace(); // Log the stack trace for debugging
            throw new RuntimeException("Error during encryption: " + e.getMessage(), e);
        }
    }

    /* @return The decrypted plaintext string. */
    public String decrypt(String encryptedBase64) {
        try {
            byte[] ivAndEncrypted = Base64.getDecoder().decode(encryptedBase64);
            if (ivAndEncrypted.length < IV_SIZE_BYTES) {
                throw new IllegalArgumentException("Invalid encrypted data - too short");
            }
            byte[] iv = Arrays.copyOfRange(ivAndEncrypted, 0, IV_SIZE_BYTES);
            byte[] encrypted = Arrays.copyOfRange(ivAndEncrypted, IV_SIZE_BYTES, ivAndEncrypted.length);
            Cipher cipher = Cipher.getInstance(MODE_PADDING);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }

    private byte[] generateRandomIV() {
        byte[] iv = new byte[IV_SIZE_BYTES];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(iv);
        return iv;
    }
}