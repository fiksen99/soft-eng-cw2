package com.acmetelecom;

public class BillEntry {
    public final String time;
    public final String number;
    public final String duration;
    public final String cost;

    public BillEntry(String time, String number, String duration, String cost) {
        this.time = time;
        this.number = number;
        this.duration = duration;
        this.cost = cost;
    }

    public static BillEntry fromLineItem(final LineItem lineItem) {
        return new BillEntry(lineItem.date(), lineItem.callee(), lineItem.durationMinutes(), lineItem.cost().toString());
    }
}
