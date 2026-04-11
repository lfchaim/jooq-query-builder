// src/main/java/com/querybuilder/util/CryptoUtil.java
package com.querybuilder.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CryptoUtil {

    @Value("${query-builder.crypto.secret-key}")
    private String secretKey;

    @Value("${query-builder.crypto.algorithm}")
    private String algorithm;

    public String decrypt(String encryptedValue) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedValue);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar a senha do DataSet", e);
        }
    }

    public String encrypt(String plainValue) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainValue.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }
}