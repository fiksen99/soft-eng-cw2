package com.acmetelecom;

import com.acmetelecom.customer.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {

    private final List<CallEvent> callLog;
    private final CustomerDatabase customerDb;
    private final TariffLibrary tariffLib;
    private final BillGeneratorFactory billGeneratorFact;

    public BillingSystem() {
        this(CentralCustomerDatabase.getInstance(), CentralTariffDatabase.getInstance(), new BillGeneratorRealFactory());
    }

    public BillingSystem(CustomerDatabase db, TariffLibrary lib, BillGeneratorFactory billGeneratorFact) {
        callLog = new ArrayList<CallEvent>();
        customerDb = db;
        tariffLib = lib;
        this.billGeneratorFact = billGeneratorFact;
    }

    public void callInitiated(String caller, String callee) {
        callLog.add(new CallStart(caller, callee));
    }

    public void callCompleted(String caller, String callee) {
        callLog.add(new CallEnd(caller, callee));
    }

    public void callInitiated(String caller, String callee, long time) {
        callLog.add(new CallStart(caller, callee, time));
    }

    public void callCompleted(String caller, String callee, long time) {
        callLog.add(new CallEnd(caller, callee, time));
    }

    public void createCustomerBills() {
        List<Customer> customers = customerDb.getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
        callLog.clear();
    }

    CallEvent lastCallEvent() {
        return callLog.get(callLog.size()-1).copy();
    }

    private void createBillFor(Customer customer) {
        List<CallEvent> customerEvents = new ArrayList<CallEvent>();
        for (CallEvent callEvent : callLog) {
            if (callEvent.getCaller().equals(customer.getPhoneNumber())) {
                customerEvents.add(callEvent);
            }
        }

        List<Call> calls = new ArrayList<Call>();

        CallEvent start = null;
        for (CallEvent event : customerEvents) {
            if (event instanceof CallStart) {
                start = event;
            }
            if (event instanceof CallEnd && start != null) {
                calls.add(new Call(start, event));
                start = null;
            }
        }

        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();

        for (Call call : calls) {

            Tariff tariff = tariffLib.tarriffFor(customer);

            BigDecimal cost;

            DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
            if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime()) && call.durationSeconds() < 12 * 60 * 60) {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
            } else {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
            }

            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

        this.billGeneratorFact.createBillGenerator().send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }


    static class LineItem {
        private Call call;
        private BigDecimal callCost;

        public LineItem(Call call, BigDecimal callCost) {
            this.call = call;
            this.callCost = callCost;
        }

        public String date() {
            return call.date();
        }

        public String callee() {
            return call.callee();
        }

        public String durationMinutes() {
            return "" + call.durationSeconds() / 60 + ":" + String.format("%02d", call.durationSeconds() % 60);
        }

        public BigDecimal cost() {
            return callCost;
        }
    }
}
