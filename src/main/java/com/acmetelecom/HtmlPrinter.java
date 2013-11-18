package com.acmetelecom;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.util.LineItem;

import java.util.List;

class HtmlPrinter implements Printer {

    private static Printer instance = new HtmlPrinter();

    private HtmlPrinter() {
    }

    public static Printer getInstance() {
        return instance;
    }

    // Deprecating this to discourage use of it, since it opens a <table> tag without closing it
    @Deprecated
    public void printHeading(String name, String phoneNumber, String pricePlan) {
        beginHtml();
        System.out.println(h2(name + "/" + phoneNumber + " - " + "Price Plan: " + pricePlan));
        beginTable();
    }

    /**
     * This is the new preferred method to print the heading. It does not open HTML
     * tags without closing them and just concentrates on printing the heading.
     */
    public void printHeader(String name, String phoneNumber, String pricePlan) {
        System.out.println(h2(name + "/" + phoneNumber + " - " + "Price Plan: " + pricePlan));
    }

    private void beginTable() {
        System.out.println("<table border=\"1\">");
        System.out.println(tr(th("Time") + th("Number") + th("Duration") + th("Cost")));
    }

    private void endTable() {
        System.out.println("</table>");
    }

    private String h2(String text) {
        return "<h2>" + text + "</h2>";
    }

    /**
     * Outputs HTML table of all the LineItems in the list
     */
    public void printItemTable(List<LineItem> items) {
        beginTable();
        for (LineItem item : items) {
            printItem(item.date(), item.callee(), item.durationMinutes(), MoneyFormatter.penceToPounds(item.cost()));
        }
        endTable();
    }

    public void printItem(String time, String callee, String duration, String cost) {
        System.out.println(tr(td(time) + td(callee) + td(duration) + td(cost)));
    }

    private String tr(String text) {
        return "<tr>" + text + "</tr>";
    }

    private String th(String text) {
        return "<th width=\"160\">" + text + "</th>";
    }

    private String td(String text) {
        return "<td>" + text + "</td>";
    }

    // Deprecating this to discourage use of it, since it closes a <table> tag without opening it
    @Deprecated
    public void printTotal(String total) {
        endTable();
        System.out.println(h2("Total: " + total));
        endHtml();
    }

    /**
     * The new preferred method for printing the total, without closing tags
     * that weren't opened here.
     */
    public void printTotalFooter(String total) {
        System.out.println(h2("Total: " + total));
    }

    /**
     * Prints the whole bill as HTML to System.out
     */
    public void printBill(Customer customer, List<LineItem> calls, String totalBill) {
        beginHtml();
        printHeader(customer.getFullName(), customer.getPhoneNumber(), customer.getPricePlan());
        printItemTable(calls);
        printTotalFooter(totalBill);
        endHtml();
    }

    private void beginHtml() {
        System.out.println("<html>");
        System.out.println("<head></head>");
        System.out.println("<body>");
        System.out.println("<h1>");
        System.out.println("Acme Telecom");
        System.out.println("</h1>");
    }

    private void endHtml() {
        System.out.println("</body>");
        System.out.println("</html>");
    }
}
