Feature: Basic Billing

Background:
    Given the following tariffs:
        |Name 	| Unit Price |
        |OffPeak 0.50       |
        |Peak    | 1.00       |
        |Bread 	| 0.75       |


Scenario: Receipt Printing
    When the customer purchases Milk
    And  the customer purchases Cheese
    And  the customer purchases Milk
    And  the customer purchases Bread

    Then the receipt shows:
        | Count | Product Name | Unit Price | Line Total |
        |     2 | Milk         |       0.50 |       1.00 |
        |     1 | Cheese       |       1.00 |       1.00 |
        |     1 | Bread        |       0.75 |       0.75 |
    And total 2.75


Scenario: Running Total
    When the customer purchases Milk
    Then the running total is   0.50
    And  the customer purchases  Cheese
    Then the running total is   1.50
    And  the customer purchases  Milk
    Then the running total is   2.00
    And  the customer purchases  Bread
    Then the running total is   2.75
