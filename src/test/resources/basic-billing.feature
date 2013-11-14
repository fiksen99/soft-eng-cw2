Feature: Billing

Background:
    Given the following customer database:
        |   FullName  | PhoneNumber | PricePlan |
        | Alan        |     001     | Standard  |
        | Steve       |     002     | Standard  |

Scenario: Offpeak-Offpeak
    When 001 calls 002 at 10/26/2013, 03:15
  	And 001 ends call with 002 at 10/26/2013, 3:39

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/26/2013 03:15 | 002         | 0:24      |   0.24    |
  	And total 0.24

Scenario: Peak-Peak
    When 001 calls 002 at 10/25/2013, 15:00
  	And 001 ends call with 002 at 10/25/2013, 18:14

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/25/2013 15:00 | 002         | 3:14      | 3.88      |
  	And total 3.88

Scenario: Peak-Offpeak
    When 001 calls 002 at 10/25/2013, 15:00
  	And 001 ends call with 002 at 10/25/2013, 19:14

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/25/2013 15:00 | 002         | 4:14      | 4.94      |
  	And total 4.94

Scenario: Offpeak-Peak
    When 001 calls 002 at 10/25/2013, 5:00
  	And 001 ends call with 002 at 10/25/2013, 9:14

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/25/2013 05:00 | 002         | 3:14      | 4.02      |
  	And total 4.02

Scenario: Peak-Offpeak-Peak
    When 001 calls 002 at 10/25/2013, 18:00
  	And 001 ends call with 002 at 10/26/2013, 8:14

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/25/2013 18:00 | 002         | 14:14     | 9.88      |
  	And total 9.88
  	
Scenario: Offpeak-Peak-Offpeak
    When 001 calls 002 at 10/25/2013, 6:00
  	And 001 ends call with 002 at 10/25/2013, 20:00

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/25/2013 06:00 | 002         | 14:00     | 15.60     |
  	And total 15.60

Scenario: No calls
    
    Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
  	And total 0.0
  	
  Scenario: Multiple calls
    When 001 calls 002 at 10/25/2013, 6:00
  	And 001 ends call with 002 at 10/25/2013, 7:00
  	
  	And 001 calls 002 at 10/25/2013, 9:00
  	And 001 ends call with 002 at 10/25/2013, 10:00

	Then the bill shows:
		Alan/001 - Price Plan: Basic
	    |     Time        | Number      | Duration  |   Cost    |
	    |10/25/2013 06:00 | 002         | 1:00      |  0.60     |
	    |10/25/2013 09:00 | 002         | 1:00      |  1.20     |
  	And total 1.80