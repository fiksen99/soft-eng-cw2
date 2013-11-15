package com.acmetelecom;

import cucumber.api.DataTable;
import cucumber.api.Format;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.acmetelecom.Call;
import com.acmetelecom.DaytimePeakPeriod;
import com.acmetelecom.BillingSystem;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.TariffLibrary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CucumberSteps {
    private final CustomerDatabase customersDb = Mockito.mock(CustomerDatabase.class);
    private final TariffLibrary tariffsDb = Mockito.mock(TariffLibrary.class);
    private final int peakStartHour = 7;
    private final int peakEndHour = 19;
    private final DaytimePeakPeriod period = new DaytimePeakPeriod(peakStartHour, peakEndHour);
    private final BillingSystem billingSystem = new BillingSystem(customersDb, tariffsDb, BillGeneratorMockFactory.getInstance());


    @Given("the following customer database")
    public void setUpCustomerDatabaseForTest(DataTable customersTable) {
    	List<Customer> customers = new ArrayList<Customer>();

    	for (Map<String, String> row : customersTable.asMaps()) {
        		String phoneNumber = row.get("PhoneNumber");
        		String pricePlan = row.get("PricePlan");
        		Customer customer = new Customer(row.get("FullName"), phoneNumber, pricePlan);
        		customers.add(customer);

            	Mockito.when(tariffsDb.tarriffFor(Mockito.argThat(new PlanMatcher(customer)))).thenReturn(Tariff.valueOf(pricePlan));
        }

        Mockito.stub(customersDb.getCustomers()).toReturn(customers);
    }

    private class PlanMatcher extends ArgumentMatcher<Customer> {
    	private Customer expectedCustomer;

		public PlanMatcher(Customer expectedCustomer) {
			this.expectedCustomer = expectedCustomer;
		}

    	@Override
		public boolean matches(Object obj) {
    		if (expectedCustomer == obj) return true;
            if (!(obj instanceof Customer)) return false;

            Customer givenCustomer = (Customer) obj;

            if (givenCustomer.getPricePlan() != null
                    ? !givenCustomer.getPricePlan().equals(expectedCustomer.getPricePlan())
                    : expectedCustomer.getPricePlan() != null) return false;

            return true;  //To change body of implemented methods use File | Settings | File Templates.
		}
    }


	@When("\\s calls \\s at \"(.*)\"")
    public void startCall(String caller, String callee, @Format("dd-MM-yyyy, HH:mm") Date date) {
    	billingSystem.callInitiated(caller, callee, date.getTime());
    }

    @And("\\d+ ends call with \\d+ at \"(.*)\"")
    public void endCall(String caller, String callee, @Format("dd-MM-yyyy, HH:mm") Date date) {
        billingSystem.callCompleted(caller, callee, date.getTime());
    }

    @Then(value = "")
    public void createBills() {

    }

    @Then("total\\s+(\\d+(?:\\.\\d+)?)")
    public void checkTotal(BigDecimal expectedTotal) {

    }

}