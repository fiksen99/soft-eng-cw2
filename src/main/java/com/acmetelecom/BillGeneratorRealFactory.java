package com.acmetelecom;

public class BillGeneratorRealFactory extends BillGeneratorFactory {

    @SuppressWarnings("deprecation")
    @Override
    public BillGenerator createBillGenerator() {
        return new BillGenerator();
    }

}
