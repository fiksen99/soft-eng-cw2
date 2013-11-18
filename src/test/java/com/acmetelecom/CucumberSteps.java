package com.acmetelecom;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.util.LineItem;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CucumberSteps {
    private final CustomerDatabase customersDb = Mockito.mock(CustomerDatabase.class);
    private final TariffLibrary tariffsDb = Mockito.mock(TariffLibrary.class);
    private final int peakStartHour = 7;
    private final int peakEndHour = 19;
    private final DaytimePeakPeriod period = new DaytimePeakPeriod(peakStartHour, peakEndHour);
    private final BillingSystem billingSystem = new BillingSystem(customersDb, tariffsDb, new BillGeneratorMockFactory());

    private static final Locale LOCALE = Locale.UK;

    @Given("the following customer database:")
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

	@When("^(.+) calls (.+) at \"([^\"]*)\"$")
    public void startCall(String caller, String callee, String date) {
		DateTime time = DateTime.parse(date, DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss"));
    	billingSystem.callInitiated(caller, callee, time.getMillis());

    	System.out.println("startCall: " + time.dayOfMonth().get() + ", " + time.getHourOfDay());
    }

    @And("^(.+) ends call with (.+) at \"(.*)\"$")
    public void endCall(String caller, String callee, String date) {
    	DateTime time = DateTime.parse(date, DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss"));
        billingSystem.callCompleted(caller, callee, time.getMillis());
    }

    @Then("the bill for (.+) with number (.+) and plan (.+) shows:")
    public void createBills(String name, String number, String plan, DataTable expectedLines) {
    	System.out.println("name " + name + " number " + number + " plan " + plan);
    	Customer customer = new Customer(name, number, plan);

        List<LineItem> actualLines = new ArrayList<LineItem>();
        List<LineItem> items = billingSystem.createBillFor(customer).getSnd();

        System.out.println("called createBillFor");
        System.out.println("size: " + items.size());

        for (LineItem line: items) {
            actualLines.add(line);
            System.out.println(line.callee() + " " + line.date() + " " + line.durationMinutes() + " " + line.cost());
        }


        expectedLines.diff(actualLines);
    }

    @Then("total (\\d+(?:\\.\\d+)?)")
    public void checkTotal(BigDecimal expectedTotal) {
        expectedTotal = expectedTotal.setScale(0, RoundingMode.HALF_UP);
    	Customer customer = new Customer("Alan", "001", "Standard");	//TODO
    	BigDecimal actualTotal = billingSystem.createBillFor(customer).getFst();
        assertEquals("bill total", expectedTotal, actualTotal);
    }

}