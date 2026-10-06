package com.erp.insurance.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}