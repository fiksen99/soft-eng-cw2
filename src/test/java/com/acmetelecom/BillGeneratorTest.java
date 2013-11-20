package com.acmetelecom;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.tidy.Tidy;

import com.acmetelecom.customer.Customer;

public class BillGeneratorTest {
    private ByteArrayOutputStream tempOut;
    private PrintStream oldOut;

    @Test
    public void test() throws Exception {
        BillGenerator billGen = BillGeneratorMockFactory.getInstance().createBillGenerator();
        String john = "John Smith";
        String helen = "Helen Mirren";

        // Set up dummy parameters:
        Customer customer = new Customer("John Smith", "1234", "expensivePlan");
        String totalBill = "totalBill";
        List<LineItem> items = new LinkedList<LineItem>();

        // Populate items:
        items.add(new LineItem(
                    new Call(
                            new CallStart(john, helen),
                            new CallEnd(john, helen)
                    ),
                    new BigDecimal(100)
                   ));

        items.add(new LineItem(
                new Call(
                        new CallStart(helen, john),
                        new CallEnd(helen, john)
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
        // Put the old System.out back in place
        System.out.flush();
        System.setOut(oldOut);
    }

    /**
     * Parses HTML in System.out and checks for errors, but ignores warnings since
     * the existing implementation spreads opening and closing HTML tags over different
     * methods.
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
