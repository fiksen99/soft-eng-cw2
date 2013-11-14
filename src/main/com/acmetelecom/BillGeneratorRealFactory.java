package com.acmetelecom;

public class BillGeneratorRealFactory extends BillGeneratorFactory {

	@Override
	public BillGenerator createBillGenerator() {
		return new BillGenerator();
	}

}
