package com.acmetelecom;

import java.math.BigDecimal;
import java.util.Locale;

class MoneyFormatter {
    public static String penceToPounds(BigDecimal pence) {
        BigDecimal pounds = pence.divide(new BigDecimal(100));

        // Note that we need to specify the "UK" locale here because some
        // other languages use ',' as the decimal separator.
        return String.format(Locale.UK, "%.2f", pounds.doubleValue());
    }
}
