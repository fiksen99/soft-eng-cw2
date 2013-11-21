package com.acmetelecom;

import java.math.BigDecimal;
import java.util.List;

/**
 * Simple wrapper class for a BigDecimal (cost) and a list of
 * BillingSystem.LineItems.
 * Avoids further refactoring of the initial BillingSystem code to separate the
 * computation of cost and items into their own functions. Rather, both
 * computations are done in BillingSystem.getBill(), which was copied directly
 * from the original createBillFor(Customer) method.
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
