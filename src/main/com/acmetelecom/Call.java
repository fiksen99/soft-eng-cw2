package com.acmetelecom;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Call {
    private CallEvent start;
    private CallEvent end;

    public Call(CallEvent start, CallEvent end) {
        this.start = start;
        this.end = end;
    }

    public String callee() {
        return start.getCallee();
    }

    public int durationSeconds() {
        return (int) (((end.time() - start.time()) / 1000));
    }

    public String date() {
        // Need to specify UK locale here to ensure date gets formatted the same way across all systems.
        final DateFormat formatter = SimpleDateFormat.getDateTimeInstance(
                SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale.UK);
        return formatter.format(new Date(start.time()));
    }

    public Date startTime() {
        return new Date(start.time());
    }

    public Date endTime() {
        return new Date(end.time());
    }
}
