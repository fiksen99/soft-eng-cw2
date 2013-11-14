package com.acmetelecom;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BillingSystemTest {
    private BillingSystem billingSystem;
    private static final String john = "John Smith";
    private static final String helen = "Helen Mirren";

    @Test
    public void testCallInitiated() throws Exception {
        billingSystem.callInitiated(john, helen);
        verifyLastEvent(john, helen);
    }

    @Test
    public void testCallCompleted() throws Exception {
        billingSystem.callCompleted(john, helen);
        verifyLastEvent(john, helen);
    }

    @Test
    public void testCreateCustomerBills() throws Exception {

    }

    @Before
    public void setUp() {
        CustomerDatabase mockDb = mock(CustomerDatabase.class);
        TariffLibrary mockTariffLib = mock(TariffLibrary.class);

        // set up customers
        Customer customerJohn = new Customer(john, "1234", "Standard");
        Customer customerHelen = new Customer(helen, "5678", "Business");

        // populate customer db and mock behaviour
        List<Customer> customers = new LinkedList<Customer>();
        customers.add(customerJohn);
        customers.add(customerHelen);

        doReturn(customers).when(mockDb).getCustomers();

        // mock tariff library behaviour
        doReturn(Tariff.Standard).when(mockTariffLib).tarriffFor(customerJohn);
        doReturn(Tariff.Business).when(mockTariffLib).tarriffFor(customerHelen);

        billingSystem = new BillingSystem(mockDb, mockTariffLib);
    }

    /**
     * Makes sure last event added to log conforms to the call we initiated
     */
    private void verifyLastEvent(String caller, String callee) {
        CallEvent lastEvent = billingSystem.lastCallEvent();
        assertEquals(lastEvent.getCaller(), caller);
        assertEquals(lastEvent.getCallee(), callee);
    }
}
