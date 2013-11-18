package com.acmetelecom;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.util.LineItem;

import java.util.List;

public interface Printer {

    void printHeading(String name, String phoneNumber, String pricePlan);
    void printHeader(String name, String phoneNumber, String pricePlan);

    void printItem(String time, String callee, String duration, String cost);
    void printItemTable(List<LineItem> items);

    void printTotal(String total);
    void printTotalFooter(String total);

    void printBill(Customer customer, List<LineItem> calls, String totalBill);
}
