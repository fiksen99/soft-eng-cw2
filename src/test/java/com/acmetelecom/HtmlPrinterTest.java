package com.acmetelecom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class HtmlPrinterTest {
    private ByteArrayOutputStream tempOut;
    private PrintStream oldOut;

    @Test
    public void testGetInstance() throws Exception {
        HtmlPrinter.getInstance();
    }

    @Test
    public void testPrintHeading() throws Exception {
        Printer printer = HtmlPrinter.getInstance();

        // Print heading
        printer.printHeading("John Smith", "1234", "expensivePlan");

        checkHTML();
    }

    @Test
    public void testPrintItem() throws Exception {
        Printer printer = HtmlPrinter.getInstance();

        // Print item
        printer.printItem("bedTime", "John Smith", "shortTime", "aLotOfMoney");

        checkHTML();
    }

    @Test
    public void testPrintTotal() throws Exception {
        Printer printer = HtmlPrinter.getInstance();

        // Print total
        printer.printTotal("evenMoreMoney");

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
