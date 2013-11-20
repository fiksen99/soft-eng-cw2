package com.acmetelecom;

import java.math.BigDecimal;
import java.util.List;

/**
 * Simple wrapper class for a BigDecimal (cost) and a list of
 * BillingSystem.LineItems.
 *
 * @author Goncalo Soares
 *
 */
class Bill {
    private final BigDecimal cost;
    private final List<BillingSystem.LineItem> items;

    public Bill(BigDecimal cost, List<BillingSystem.LineItem> items) {
        this.cost = cost;
        this.items = items;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public List<BillingSystem.LineItem> getItems() {
        return items;
    }

}
