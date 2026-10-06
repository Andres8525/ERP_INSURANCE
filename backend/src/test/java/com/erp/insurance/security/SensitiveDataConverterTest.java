package com.erp.insurance.security;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SensitiveDataConverterTest {
    @Test
    void encryptsValuesWithDistinctRandomNonces() {
        SensitiveDataConverter converter = new SensitiveDataConverter(new byte[32]);
        String first = converter.convertToDatabaseColumn("member@example.test");
        String second = converter.convertToDatabaseColumn("member@example.test");
        assertNotEquals(first, second);
        assertEquals("member@example.test", converter.convertToEntityAttribute(first));
    }

    @Test
    void preservesNullValues() {
        SensitiveDataConverter converter = new SensitiveDataConverter(new byte[32]);
        assertEquals(null, converter.convertToDatabaseColumn(null));
        assertEquals(null, converter.convertToEntityAttribute(null));
    }

    @Test
    void rejectsTamperedCiphertext() {
        SensitiveDataConverter converter = new SensitiveDataConverter(new byte[32]);
        String encrypted = converter.convertToDatabaseColumn("protected data");
        String[] parts = encrypted.split("\\.", 2);
        byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
        payload[payload.length - 1] ^= 1;
        String tampered = parts[0] + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(payload);
        assertThrows(IllegalStateException.class, () -> converter.convertToEntityAttribute(tampered));
    }
}