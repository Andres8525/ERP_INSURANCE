package com.erp.insurance.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class SensitiveDataConverter implements AttributeConverter<String, String> {
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final byte[] testKey;

    public SensitiveDataConverter() {
        this.testKey = null;
    }

    SensitiveDataConverter(byte[] testKey) {
        if (testKey.length != 32) throw new IllegalArgumentException("AES-256 key must be 32 bytes");
        this.testKey = testKey.clone();
    }

    @Override
    public String convertToDatabaseColumn(String value) {
        if (value == null) return null;
        try {
            byte[] iv = new byte[IV_LENGTH];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key(), new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return "v1." + Base64.getUrlEncoder().withoutPadding().encodeToString(
                    ByteBuffer.allocate(iv.length + encrypted.length).put(iv).put(encrypted).array());
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Could not encrypt sensitive data", exception);
        }
    }

    @Override
    public String convertToEntityAttribute(String stored) {
        if (stored == null) return null;
        try {
            String[] parts = stored.split("\\.", 2);
            if (parts.length != 2 || !"v1".equals(parts[0])) {
                throw new IllegalStateException("Unsupported encrypted data format");
            }
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            if (payload.length <= IV_LENGTH) throw new IllegalStateException("Invalid encrypted data");
            ByteBuffer buffer = ByteBuffer.wrap(payload);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key(), new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Could not decrypt sensitive data", exception);
        }
    }

    private SecretKeySpec key() {
        if (testKey != null) return new SecretKeySpec(testKey, "AES");
        String encoded = System.getenv("ENCRYPTION_KEY");
        if (encoded == null || encoded.isBlank()) {
            throw new IllegalStateException("ENCRYPTION_KEY is required to access encrypted data");
        }
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("ENCRYPTION_KEY must be Base64", exception);
        }
        if (bytes.length != 32) throw new IllegalStateException("ENCRYPTION_KEY must decode to exactly 32 bytes");
        return new SecretKeySpec(bytes, "AES");
    }
}