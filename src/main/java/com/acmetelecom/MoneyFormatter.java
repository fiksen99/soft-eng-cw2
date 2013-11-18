package com.acmetelecom;

import java.math.BigDecimal;
import java.util.Locale;

class MoneyFormatter {

    private final Currency currency;
    private final BigDecimal value;

    enum Currency { GBP }

    private MoneyFormatter(Currency currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    public static MoneyFormatter gbp(BigDecimal value) {
        return new MoneyFormatter(Currency.GBP, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyFormatter money = (MoneyFormatter) o;

        if (value != money.value) return false;
        if (currency != money.currency) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + value.intValue();
        return result;
    }

    @Override
    public String toString() {
        return currency.name() + " " + value;
    }

    public static String penceToPounds(BigDecimal pence) {
        BigDecimal pounds = pence.divide(new BigDecimal(100));

        // Note that we need to specify the "UK" locale here because some
        // other languages use ',' as the decimal separator.
        return String.format(Locale.UK, "%.2f", pounds.doubleValue());
    }
}
