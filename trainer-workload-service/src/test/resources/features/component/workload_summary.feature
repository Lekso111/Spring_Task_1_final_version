Feature: Trainer workload summary
  As the workload service
  I want to keep each trainer monthly training hours up to date
  So that summaries can be reported per year and month

  Scenario: Recording hours for a brand new trainer
    When an ADD workload event for trainer "john.doe" of 3.5 hours on "2024-05-10" is received
    Then the monthly summary for "john.doe" reports 3.5 hours for 2024-05

  Scenario: Accumulating hours within the same month
    Given trainer "john.doe" has already recorded 2.0 hours on "2024-05-01"
    When an ADD workload event for trainer "john.doe" of 4.0 hours on "2024-05-20" is received
    Then the monthly summary for "john.doe" reports 6.0 hours for 2024-05

  Scenario: Removing part of the recorded hours
    Given trainer "john.doe" has already recorded 10.0 hours on "2024-05-10"
    When a DELETE workload event for trainer "john.doe" of 4.0 hours on "2024-05-10" is received
    Then the monthly summary for "john.doe" reports 6.0 hours for 2024-05

  Scenario: Removing more hours than recorded never goes negative
    Given trainer "john.doe" has already recorded 2.0 hours on "2024-05-10"
    When a DELETE workload event for trainer "john.doe" of 5.0 hours on "2024-05-10" is received
    Then the monthly summary for "john.doe" reports 0.0 hours for 2024-05

  Scenario: Requesting a summary for an unknown trainer
    When the workload summary for "ghost" is requested
    Then the summary is empty and the trainer is marked inactive
