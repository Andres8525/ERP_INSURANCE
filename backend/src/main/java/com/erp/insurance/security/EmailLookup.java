package com.erp.insurance.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HexFormat;

public final class EmailLookup {
    private EmailLookup() {}

    public static String hash(String email) {
        try {
            byte[] key = Base64.getDecoder().decode(System.getenv("ENCRYPTION_KEY"));
                byte[] lookupKey = MessageDigest.getInstance("SHA-256").digest(
                    java.nio.ByteBuffer.allocate(key.length + 19).put(key).put("email-lookup-v1".getBytes(StandardCharsets.UTF_8)).array());
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(lookupKey, "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Could not derive patient email lookup hash", exception);
        }
    }
}