package com.acmetelecom;

import com.acmetelecom.customer.Customer;

import java.util.List;

public class BillGenerator {

    /**
     * NOTE: We deprecate this constructor to make depending components aware of the fact that this is not the recommended
     * way of instantiating a BillGenerator anymore. However we are not planning on deleting it in the future, since we're
     * still using it in our factory implementation. So the plan for the future would be to turn this into a package private
     * constructor that can be called from our factory, rather than entirely deleting it.
     * The reason for not turning it into a private constructor straight away is that other components might still depend
     * on it, and we don't want to silently break them.
     */
    @Deprecated
    public BillGenerator() {

    }

    public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        HtmlPrinter.getInstance().printBill(customer, calls, totalBill);
    }
}
