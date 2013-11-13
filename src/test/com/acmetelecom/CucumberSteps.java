package com.acmetelecom;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.acmetelecom.Call;
import com.acmetelecom.DaytimePeakPeriod;
import com.acmetelecom.BillingSystem;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CucumberSteps {
	private CustomerDatabase customersDb = Mockito.mock(CustomerDatabase.class);
    private final Map<String, List<Call>> callsByNumber = new HashMap<String, List<Call>>();
    private final int peakStartHour = 7;
    private final int peakEndHour = 19;
    private final DaytimePeakPeriod period = new DaytimePeakPeriod(peakStartHour, peakEndHour);
    private BillingSystem billingSystem = new BillingSystem(); 


}