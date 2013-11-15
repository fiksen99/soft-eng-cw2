package com.acmetelecom;

public abstract class CallEvent {
    private String caller;
    private String callee;
    private long time;

    public CallEvent(String caller, String callee, long timeStamp) {
        this.caller = caller;
        this.callee = callee;
        this.time = timeStamp;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public long time() {
        return time;
    }

    abstract CallEvent copy();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallEvent)) return false;

        CallEvent callEvent = (CallEvent) o;

        if (time != callEvent.time) return false;
        if (callee != null ? !callee.equals(callEvent.callee) : callEvent.callee != null) return false;
        if (caller != null ? !caller.equals(callEvent.caller) : callEvent.caller != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = caller != null ? caller.hashCode() : 0;
        result = 31 * result + (callee != null ? callee.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
