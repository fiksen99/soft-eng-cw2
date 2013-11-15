package com.acmetelecom;

import com.acmetelecom.customer.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BillGeneratorTest {
    private ByteArrayOutputStream tempOut;
    private PrintStream oldOut;

    @Test
    public void test() throws Exception {
        BillGenerator billGen = new BillGenerator();

        // Set up dummy parameters:
        Customer customer = new Customer("John Smith", "1234", "expensivePlan");
        String totalBill = "totalBill";
        List<BillingSystem.LineItem> items = new LinkedList<BillingSystem.LineItem>();

        // Populate items:
        items.add(new BillingSystem.LineItem(
                    new Call(
                            new CallStart("John Smith", "Hellen Mirren"),
                            new CallEnd("John Smith", "Helen Mirren")
                    ),
                    new BigDecimal(100)
                   ));

        items.add(new BillingSystem.LineItem(
                new Call(
                        new CallStart("Helen Mirren", "John Smith"),
                        new CallEnd("Helen Mirren", "John Smith")
                ),
                new BigDecimal(100)
        ));

        // generate bill and check output:
        billGen.send(customer, items, totalBill);
        checkHTML();
    }

    @Before
    public final void setUp() {
        // Create a stream to hold the output
        tempOut = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(tempOut);

        // Save the old System.out
        oldOut = System.out;

        // Replace system.out stream
        System.setOut(ps);
    }

    @After
    public final void tearDown() {
        // Put things back
        System.out.flush();
        System.setOut(oldOut);
    }

    /**
     * Parses HTML in System.out and checks for errors
     * @throws Exception
     */
    private void checkHTML() throws Exception {
        // create JTidy object to parse HTML
        Tidy tidy = new Tidy();
        StringWriter writer = new StringWriter();
        tidy.parse(new StringReader(tempOut.toString()), writer);

        assertEquals(0, tidy.getParseErrors());
    }
}
