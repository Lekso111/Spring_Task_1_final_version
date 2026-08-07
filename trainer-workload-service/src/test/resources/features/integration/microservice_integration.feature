Feature: Integration between gym-main-service and trainer-workload-service
  As the gym platform
  I want workload events published by the main service to be consumed by the workload service
  So that trainer workload stays consistent across microservices

  Scenario: A published training event updates the trainer workload
    When the main service publishes an ADD workload event for trainer "john.doe" of 5.0 hours on "2024-06-15"
    Then the trainer "john.doe" eventually has 5.0 hours recorded for 2024-06

  Scenario: A delete event reduces the recorded workload
    When the main service publishes an ADD workload event for trainer "john.doe" of 8.0 hours on "2024-06-15"
    And the main service publishes a DELETE workload event for trainer "john.doe" of 3.0 hours on "2024-06-15"
    Then the trainer "john.doe" eventually has 5.0 hours recorded for 2024-06

  Scenario: An invalid event is rejected and does not change the workload
    When the main service publishes a workload event with a blank username of 5.0 hours on "2024-06-15"
    Then the invalid event is moved to the dead letter queue
    And no workload is stored for trainer ""
