package com.acmetelecom;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;

public class MoneyFormatterTest {
    /**
     * Tests conversion of pence to pounds with 2 significant figures after the decimal
     * separator. Also tests that the decimal separator is '.'
     * @throws Exception
     */
    @Test
    public void testPenceToPounds() throws Exception {
        assertEquals("0.00", MoneyFormatter.penceToPounds(new BigDecimal(0)));
        assertEquals("0.50", MoneyFormatter.penceToPounds(new BigDecimal(50)));
        assertEquals("1.00", MoneyFormatter.penceToPounds(new BigDecimal(100)));

        assertEquals("0.00", MoneyFormatter.penceToPounds(new BigDecimal(-0)));
        assertEquals("-0.50", MoneyFormatter.penceToPounds(new BigDecimal(-50)));
        assertEquals("-1.00", MoneyFormatter.penceToPounds(new BigDecimal(-100)));
    }
}
