package com.acmetelecom;

import org.junit.Test;
import static org.junit.Assert.*;

public class CallEventTest {
    private static final String CALLER = "1234";
    private static final String CALLEE = "5678";
    private static final long TIME = 0;

    @Test
    public void testCallStart() {
        testCallEvent(new CallStart(CALLER, CALLEE, TIME));
    }

    @Test
    public void testCallEnd() {
        testCallEvent(new CallEnd(CALLER, CALLEE, TIME));
    }

    private void testCallEvent(CallEvent event) {
        assertEquals(CALLER, event.getCaller());
        assertEquals(CALLEE, event.getCallee());
        assertEquals(TIME, event.time());
    }

}
