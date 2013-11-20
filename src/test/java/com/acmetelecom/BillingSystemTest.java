package com.acmetelecom;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;

public class BillingSystemTest {
    private BillingSystem billingSystem;
    private static final String john = "1234";
    private static final String helen = "5678";

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

    @Before
    public void setUp() {
        CustomerDatabase mockDb = mock(CustomerDatabase.class);
        TariffLibrary mockTariffLib = mock(TariffLibrary.class);

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
