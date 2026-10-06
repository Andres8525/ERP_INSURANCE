package com.erp.insurance.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HexFormat;

public final class EmailLookup {
    private EmailLookup() {}

    public static String hash(String email) {
        try {
            byte[] key = Base64.getDecoder().decode(System.getenv("ENCRYPTION_KEY"));
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Could not derive patient email lookup hash", exception);
        }
    }
}