package com.acmetelecom;

public class CallEnd extends CallEvent {
    public CallEnd(String caller, String callee) {
        this(caller, callee, System.currentTimeMillis());
    }

    CallEnd(final String caller, final String callee, final long timestamp) {
        super(caller, callee, timestamp);
    }
}
