package com.acmetelecom;

import java.util.Calendar;
import java.util.Date;

/**
 * Determine when a Date object is outside of peak times.
 */
class DaytimePeakPeriod {
    static final int DEFAULT_PEAK_START_HOUR = 7;
    static final int DEFAULT_PEAK_END_HOUR = 19;

    private final int peakStartHour;
    private final int peakEndHour;

    public DaytimePeakPeriod() {
        this(DEFAULT_PEAK_START_HOUR, DEFAULT_PEAK_END_HOUR);
    }

    public DaytimePeakPeriod(final int peakStartHour, final int peakEndHour) {
        this.peakStartHour = peakStartHour;
        this.peakEndHour = peakEndHour;
    }

    public boolean offPeak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < peakStartHour || hour >= peakEndHour;
    }
}
