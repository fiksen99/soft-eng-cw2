package com.acmetelecom;

import org.junit.Assert;
import org.junit.Test;

import com.acmetelecom.customer.Customer;

public class CustomerProfiling {

	@Test
	public void foo() {
		Customer cust = new Customer("Alan Smythe", "805-555-555", "cheap");
		Assert.assertEquals(cust.getFullName(), "Alan Smythe");
		Assert.assertEquals(cust.getPhoneNumber(), "805-555-555");
		Assert.assertEquals(cust.getPricePlan(), "cheap");
	}
}
