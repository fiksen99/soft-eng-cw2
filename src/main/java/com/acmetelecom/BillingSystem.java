package com.acmetelecom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;

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

    private Bill getBill(Customer customer, List<Call> calls){
        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();

        for (Call call : calls) {
            Tariff tariff = tariffLib.tarriffFor(customer);
            BillChargeCalculator cal = new BillChargeCalculator(tariff, call, customer);
            BigDecimal callCost = cal.billCharge();

            totalBill = totalBill.add(callCost);

            System.out.println("in BillingSystem.getBill: callee=" + call.callee() + ", date=" + call.date() + ", startTime=" + call.startTime());
            items.add(new LineItem(call, callCost));
        }

        return new Bill(totalBill, items);
    }

    Bill createBillFor(Customer customer) {
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
                System.out.println("Timestamp in BillingSystem.createBillFor: " + event.time());
                System.out.println("Event started at " + new DateTime(event.time()).toString("dd/MM/yyyy HH:mm:ss"));
                start = event;
            }
            if (event instanceof CallEnd && start != null) {
                System.out.println("Event ended at " + new DateTime(event.time()).toString("dd/MM/yyyy HH:mm:ss"));

                calls.add(new Call(start, event));
                start = null;
            }
        }

        Bill bill = getBill(customer, calls);
        this.billGeneratorFact.createBillGenerator().send(customer, bill.getItems(), MoneyFormatter.penceToPounds(bill.getCost()));

        return bill;
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
