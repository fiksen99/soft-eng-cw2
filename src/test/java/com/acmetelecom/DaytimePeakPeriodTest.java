package com.acmetelecom;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DaytimePeakPeriodTest {
    @Test
    public void testOffPeak() throws Exception {
        final int peakStartHour = 7;
        final int peakEndHour = 19;
        final DaytimePeakPeriod period = new DaytimePeakPeriod(peakStartHour, peakEndHour);

        final Calendar time = new GregorianCalendar();

        // 5h15 am.
        time.set(2013, Calendar.NOVEMBER, 13, 5, 15, 0);
        assertTrue(period.offPeak(time.getTime()));

        // 7h00 am. (peak start hour)
        time.set(2013, Calendar.NOVEMBER, 13, peakStartHour, 0, 0);
        assertFalse(period.offPeak(time.getTime()));

        // 12h00 am.
        time.set(2013, Calendar.NOVEMBER, 13, 12, 0, 0);
        assertFalse(period.offPeak(time.getTime()));

        // 19h00 am. (peak end hour)
        time.set(2013, Calendar.NOVEMBER, 13, peakEndHour, 0, 0);
        assertTrue(period.offPeak(time.getTime()));
    }
}
