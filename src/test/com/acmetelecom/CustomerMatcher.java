package com.acmetelecom;

import com.acmetelecom.customer.Customer;
import org.mockito.ArgumentMatcher;

class CustomerMatcher extends ArgumentMatcher<Customer> {
    private final Customer expectedCustomer;

    public CustomerMatcher(final Customer expectedCustomer) {
        this.expectedCustomer = expectedCustomer;
    }

    @Override
    public boolean matches(Object obj) {
        if (expectedCustomer == obj) return true;
        if (!(obj instanceof Customer)) return false;

        Customer givenCustomer = (Customer) obj;

        if (givenCustomer.getFullName() != null
                ? !givenCustomer.getFullName().equals(expectedCustomer.getFullName())
                : expectedCustomer.getFullName() != null) return false;

        if (givenCustomer.getPhoneNumber() != null
                ? !givenCustomer.getPhoneNumber().equals(expectedCustomer.getPhoneNumber())
                : expectedCustomer.getPhoneNumber() != null) return false;

        if (givenCustomer.getPricePlan() != null
                ? !givenCustomer.getPricePlan().equals(expectedCustomer.getPricePlan())
                : expectedCustomer.getPricePlan() != null) return false;

        return true;
    }
}