package com.acmetelecom;

public class CallStart extends CallEvent {
    public CallStart(String caller, String callee) {
        this(caller, callee, System.currentTimeMillis());
    }

    CallStart(final String caller, final String callee, final long timestamp) {
        super(caller, callee, timestamp);
    }
}
