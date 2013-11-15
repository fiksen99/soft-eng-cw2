package com.acmetelecom;

import org.mockito.Mockito;

public class BillGeneratorMockFactory extends BillGeneratorFactory {

    // make the factory a singleton
    private static BillGeneratorMockFactory factory;

    public static BillGeneratorMockFactory getInstance() {
        if (factory == null)
            factory = new BillGeneratorMockFactory();
        return factory;
    }

    @Override
	public BillGenerator createBillGenerator() {
		return Mockito.mock(BillGenerator.class);
	}
}