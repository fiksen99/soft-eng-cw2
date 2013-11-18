package com.acmetelecom;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BillingSystemTest {
    private BillingSystem billingSystem;
    private static final String john = "1234";
    private static final String helen = "5678";
    private static final Customer customerJohn = new Customer("John Smith", "1234", "Standard");
    private static final Customer customerHelen = new Customer("Helen Mirren", "5678", "Business");

    private BillGenerator mockBillGenerator;

    @Captor private ArgumentCaptor<Customer> customerCaptor;
    @Captor private ArgumentCaptor<List<BillingSystem.LineItem>> callsCaptor;
    @Captor private ArgumentCaptor<String> totalBillCaptor;


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

    @Ignore @Test
    public void testCreateCustomerBills() throws Exception {
        billingSystem.callInitiated(john, helen);
        billingSystem.callCompleted(john, helen);
        billingSystem.createCustomerBills();

        verify(mockBillGenerator, times(2)).send(customerCaptor.capture(), callsCaptor.capture(), totalBillCaptor.capture());

        for (int i = 0; i < 2; i++) {
            Customer customer = customerCaptor.getAllValues().get(i);
            List<BillingSystem.LineItem> calls = callsCaptor.getAllValues().get(i);
            String totalBill = totalBillCaptor.getAllValues().get(i);

            assertThat(customer, anyOf(is(new CustomerMatcher(customerJohn)),
                    is(new CustomerMatcher(customerHelen))));

            if (customer.getFullName().equals("John Smith")) {
                assertEquals("John number of calls", 1, calls.size());
                assertEquals(helen, calls.get(0).callee());
            }
            else {
                assertEquals("Helen number of calls", 0, calls.size());
            }
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        CustomerDatabase mockDb = mock(CustomerDatabase.class);
        TariffLibrary mockTariffLib = mock(TariffLibrary.class);

        // populate customer db and mock behaviour
        List<Customer> customers = new LinkedList<Customer>();
        customers.add(customerJohn);
        customers.add(customerHelen);

        doReturn(customers).when(mockDb).getCustomers();

        // mock tariff library behaviour
        when(mockTariffLib.tarriffFor(argThat(new CustomerMatcher(customerJohn)))).thenReturn(Tariff.Standard);
        when(mockTariffLib.tarriffFor(argThat(new CustomerMatcher(customerHelen)))).thenReturn(Tariff.Business);

        billingSystem = new BillingSystem(mockDb, mockTariffLib, new BillGeneratorMockFactory());
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
