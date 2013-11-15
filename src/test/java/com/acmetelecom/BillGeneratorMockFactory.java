package com.acmetelecom;

import org.mockito.Mockito;

public class BillGeneratorMockFactory extends BillGeneratorFactory {

	@Override
	public BillGenerator createBillGenerator() {
		return Mockito.mock(BillGenerator.class);
	}

}
