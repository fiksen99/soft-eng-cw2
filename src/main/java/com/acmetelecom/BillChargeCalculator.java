package com.acmetelecom;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Date;
import org.joda.time.*;

import com.acmetelecom.customer.Tariff;
import com.google.common.collect.Lists;
import com.acmetelecom.customer.*;

public class BillChargeCalculator {
	private List<ChargingRule> rules;
	private final DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
	private final Customer customer;
    private final Tariff tariff;
	private final Call call;

    public BillChargeCalculator(Tariff tariff, Call call, Customer customer) {
    	this.tariff = tariff;
    	this.call = call;
    	this.customer = customer;
        rules = Lists.newArrayList();
        setRules();
    }
    
    private int convertToSeconds(DateTime time, int peakHour) {
    	return 3600 * (peakHour - time.getHourOfDay()) +
				60 * time.getMinuteOfHour() + time.getSecondOfMinute();
    }
    
    private DateTime convertDateToDateTime (Date date) {
    	return new DateTime(date.getTime());
    }
    
    private BigDecimal computeCostForTimeAndRate(long durationSeconds, BigDecimal rate) {
    	return new BigDecimal(durationSeconds).multiply(rate);
    }
    
    private BigDecimal computeCostCrossPeriods(boolean startInPeak) {
    	BigDecimal cost = new BigDecimal(0);
    	int peakDurationSeconds, offPeakDurationSeconds;
    	
    	if (call.durationSeconds() < 12 * 60 * 60) {
    		return null; // TODO: throw error is probably better!!
    	}
    	else {
    		if (startInPeak) {
	    		// cost for peakRate on [endPeakTime - startCallTime] +
				// cost for offPeakRate on [startPeakTime - endCallTime]
	    		peakDurationSeconds = convertToSeconds(convertDateToDateTime(call.startTime()), peakPeriod.getPeakEndHour());
	    		offPeakDurationSeconds = convertToSeconds(convertDateToDateTime(call.endTime()), peakPeriod.getPeakStartHour());	
	    	}
	    	else {
	    		// cost for offPeakRate on [endOffPeakTime - startCallTime] +
				// cost for peakRate on [startOffPeakTime - endCallTime]
	    		offPeakDurationSeconds = convertToSeconds(convertDateToDateTime(call.startTime()), peakPeriod.getPeakStartHour());
	    		peakDurationSeconds = convertToSeconds(convertDateToDateTime(call.endTime()), peakPeriod.getPeakEndHour());	
	    	
	    	}
		
    		cost = computeCostForTimeAndRate(peakDurationSeconds, tariff.peakRate());
    		cost.add(computeCostForTimeAndRate(offPeakDurationSeconds, tariff.offPeakRate()));
    		return cost;
    	}
    }
 
    private BigDecimal calculateMaxCost() {
    	BigDecimal callCost = new BigDecimal(0);
    
    	if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime()) && 
    				call.durationSeconds() < 12 * 60 * 60) {
        	callCost = computeCostForTimeAndRate(call.durationSeconds(), tariff.offPeakRate());
        } 
    	else {
        	callCost = computeCostForTimeAndRate(call.durationSeconds(), tariff.peakRate());
        }
    	
    	return callCost;
    }
    
    private BigDecimal calculateExactCost() {
    	BigDecimal cost = new BigDecimal(0);
    	
    	if (!peakPeriod.offPeak(call.startTime()) && !peakPeriod.offPeak(call.endTime())) {
    		if (call.durationSeconds() > (peakPeriod.peakPeriodSeconds())) {
    			cost = computeCostCrossPeriods(true);
    		}
    		else {
    			cost = computeCostForTimeAndRate(call.durationSeconds(), tariff.peakRate());
    		}
    	}
		else if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime())) {
			if (call.durationSeconds() > (peakPeriod.offPeakPeriodSeconds())) {
				cost = computeCostCrossPeriods(false);
    		}
    		else {
    			cost = computeCostForTimeAndRate(call.durationSeconds(), tariff.offPeakRate());
    		}    	
		}
		else {
			cost = computeCostCrossPeriods(!peakPeriod.offPeak(call.startTime()));
		}
    	
    	return cost.setScale(0, RoundingMode.HALF_UP);
    }
    
    private void setRules() {
        chargeForCall(exactCost(calculateExactCost()));
        chargeForCall(maxCost(calculateMaxCost()));
    }

    private ChargingRule exactCost(final BigDecimal cost) {
        return new ChargingRule() {
            public boolean matches(Call call) {
            	return true; //TODO: match all the calls, supports easy extensibility in the future
            }

            public BigDecimal cost() {
                return cost;
            }
        };
    }
    
    private ChargingRule maxCost(final BigDecimal cost) {
        return new ChargingRule() {
            public boolean matches(Call call) {
            	return false; //TODO: matches all the calls atm - do we need a rule?
            				 // this design supports easy extensibility in the future...
            }

            public BigDecimal cost() {
                return cost;
            }
        };
    }
    
    private void chargeForCall(ChargingRule chargingRule) {
        rules.add(chargingRule);
    }

    public BigDecimal billCharge() {
        for (ChargingRule rule : rules) {
            if (rule.matches(call)) {
                return rule.cost();
            }
        }

        return new BigDecimal(0);
    }

	private interface ChargingRule {
        boolean matches(Call call);
        public BigDecimal cost();
    }
}
