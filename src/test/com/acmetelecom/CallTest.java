package com.acmetelecom;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class CallTest {
    private static final String CALLER = "447722113434";
    private static final String CALLEE = "447766511332";
    private static final long CALL_TIME_START = 1384361469276L;
    private static final long CALL_TIME_MILLIS = 60L * 5L * 1000L;
    private Call callInstance = null;

    @Before
    public void setUp() {
        final CallStart callStart = new CallStart(CALLER, CALLEE, CALL_TIME_START);
        final CallEnd callEnd = new CallEnd(CALLER, CALLEE, CALL_TIME_START + CALL_TIME_MILLIS);
        callInstance = new Call(callStart, callEnd);
    }

    @After
    public void tearDown() {
        callInstance = null;
    }


    // Could also check error cases (?) :
    // - Call ends before it starts.
    // - Caller calls himself.
    // - Negative time.

    @Test
    public void testCallee() throws Exception {
        assertEquals(CALLEE, callInstance.callee());
    }

    @Test
    public void testDurationSeconds() throws Exception {
        assertEquals(CALL_TIME_MILLIS / 1000L, callInstance.durationSeconds());
    }

    @Test
    public void testDate() throws Exception {
        assertEquals("13/11/13 16:51", callInstance.date());
    }

    @Test
    public void testStartTime() throws Exception {
        assertEquals(new Date(CALL_TIME_START), callInstance.startTime());
    }

    @Test
    public void testEndTime() throws Exception {
        assertEquals(new Date(CALL_TIME_START + CALL_TIME_MILLIS), callInstance.endTime());
    }
}
