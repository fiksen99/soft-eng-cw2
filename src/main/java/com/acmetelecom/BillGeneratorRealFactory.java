package com.acmetelecom;

public class BillGeneratorRealFactory extends BillGeneratorFactory {

    // make the factory a singleton
    private static BillGeneratorRealFactory factory;

    public static BillGeneratorRealFactory getInstance() {
        if (factory == null)
            factory = new BillGeneratorRealFactory();
        return factory;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BillGenerator createBillGenerator() {
        return new BillGenerator();
    }
}
