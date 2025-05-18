package com.auth.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtils {

    // Secret keys for HMAC and AES
    private static final String HMAC_KEY = "1234567890123456";
    private static final String AES_KEY = "1234567890123456"; // Must be 16, 24, or 32 bytes

    // Encrypt the message
    public static String encrypt(String message) throws Exception {
        // Generate AES SecretKey
        SecretKey aesKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");

        // Initialize Cipher for AES Encryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);  // Generate IV
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParams);

        // Encrypt the message
        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);

        // Generate HMAC for the encrypted message
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKey hmacKey = new SecretKeySpec(HMAC_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(hmacKey);
        byte[] hmacBytes = mac.doFinal(encryptedMessage.getBytes(StandardCharsets.UTF_8));
        String hmac = Base64.getEncoder().encodeToString(hmacBytes);

        // Combine the encrypted message and the HMAC (including IV as a prefix)
        String combined = Base64.getEncoder().encodeToString(iv) + ":" + encryptedMessage + ":" + hmac;
        return combined;
    }

    // Decrypt the message
    public static String decrypt(String combined) throws Exception {
        String[] parts = combined.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid input format.");
        }

        // Extract IV, encrypted message, and HMAC
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        String encryptedMessage = parts[1];
        String receivedHmac = parts[2];

        // Verify HMAC
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKey hmacKey = new SecretKeySpec(HMAC_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(hmacKey);
        byte[] calculatedHmacBytes = mac.doFinal(encryptedMessage.getBytes(StandardCharsets.UTF_8));
        String calculatedHmac = Base64.getEncoder().encodeToString(calculatedHmacBytes);

        if (!calculatedHmac.equals(receivedHmac)) {
            throw new Exception("Invalid HMAC: The message has been tampered with.");
        }

        // Decrypt the message using AES
        SecretKey aesKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParams);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
