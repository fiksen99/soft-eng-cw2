package com.acmetelecom;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;

public class MoneyFormatterTest {
    @Test
    public void testPenceToPounds() throws Exception {
        // Pence to pounds converts with 2 significant figures after the decimal point.
        assertEquals("0.00", MoneyFormatter.penceToPounds(new BigDecimal(0)));
        assertEquals("0.50", MoneyFormatter.penceToPounds(new BigDecimal(50)));
        assertEquals("1.00", MoneyFormatter.penceToPounds(new BigDecimal(100)));

        assertEquals("0.00", MoneyFormatter.penceToPounds(new BigDecimal(-0)));
        assertEquals("-0.50", MoneyFormatter.penceToPounds(new BigDecimal(-50)));
        assertEquals("-1.00", MoneyFormatter.penceToPounds(new BigDecimal(-100)));
    }
}
