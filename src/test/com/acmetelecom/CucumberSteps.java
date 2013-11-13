package com.acmetelecom;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.acmetelecom.Call;
import com.acmetelecom.DaytimePeakPeriod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CucumberSteps {
    private final Map<String,Call> callByNumber = new HashMap<String, Call>();
    private final DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();

    @Given("the following calls")
    public void setUpCallsForTest(List<Call> calls) {
    	callByNumber.clear();
        for (Call call : calls) {
        	callByNumber.put(call.callee(), call);
        }
    }

    @Given("the following off peak ")
    public void setUpOffPeakPeriodForTest(List<Call> calls) {
    	callByNumber.clear();
        for (Call call : calls) {
        	callByNumber.put(call.callee(), call);
        }
    }
    
    @When("the customer purchases\\s+(.+)")
    public void performPurchases(String productName) {
        transaction.productScanned(productsByName.get(productName));
    }