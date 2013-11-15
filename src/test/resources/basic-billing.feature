Feature: Billing

Background:
    Given the following customer database:
        |   FullName  | PhoneNumber | PricePlan |
        | Alan        |     001     | Standard  |
        | Steve       |     002     | Standard  |

Scenario: Offpeak-Offpeak
    When 001 calls 002 at "26/10/13 03:15:00"
    And 001 ends call with 002 at "26/10/13 03:39:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time        | Number      | Duration  |   Cost    |
        | 26/10/13 03:15  | 002         | 24:00     |   288     |
    And total 0.24

Scenario: Peak-Peak
    When 001 calls 002 at "25/10/13 15:00:00"
    And 001 ends call with 002 at "25/10/13 18:14:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time        | Number      | Duration  |   Cost    |
        |25/10/13 15:00   | 002         | 194:00    | 5,820     |
    And total 3.88

Scenario: Peak-Offpeak
    When 001 calls 002 at "25/10/13 15:00:00"
    And 001 ends call with 002 at "25/10/13 19:14:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time        | Number      | Duration  |   Cost    |
        |25/10/13 15:00   | 002         | 254:00    | 7,620     |
    And total 4.94

Scenario: Offpeak-Peak
    When 001 calls 002 at "25/10/13 05:00:00"
    And 001 ends call with 002 at "25/10/13 09:14:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time        | Number      | Duration  |   Cost    |
        |25/10/13 05:00   | 002         | 254:00    | 7,620     |
    And total 4.02

Scenario: Peak-Offpeak-Peak
    When 001 calls 002 at "25/10/13 18:00:00"
    And 001 ends call with 002 at "26/10/13 08:14:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time        | Number      | Duration  |   Cost    |
        |25/10/13 18:00   | 002         | 14:14:00  | 9.88      |
    And total 9.88
    
Scenario: Offpeak-Peak-Offpeak
    When 001 calls 002 at "25/10/13 06:00:00"
    And 001 ends call with 002 at "25/10/13 20:00:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time        | Number      | Duration  |   Cost    |
        |25/10/13 06:00   | 002         | 854:00    | 25,620    |
    And total 15.60
    
 Scenario: Multiple calls
    When 001 calls 002 at "25/10/13 06:00:00"
    And 001 ends call with 002 at "25/10/13 07:00:00"
    
    And 001 calls 002 at "25/10/13 09:00:00"
    And 001 ends call with 002 at "25/10/13 10:00:00"

    Then the bill for Alan with number 001 and plan Standard shows:
        |     Time      |   Number      | Duration  |   Cost    |
        |25/10/13 06:00 | 002           | 60:00     |  1,800    |
        |25/10/13 09:00 | 002           | 60:00     |  1,800    |
    And total 1.80