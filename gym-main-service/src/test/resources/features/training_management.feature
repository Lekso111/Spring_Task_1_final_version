Feature: Training management
  As the gym service
  I want to record trainings
  So that trainer workload is kept up to date

  Scenario: Adding a training publishes a workload event
    Given a trainee "john.doe1" named "john" "doe" is registered
    And a trainer "jane.smith1" named "jane" "smith" is registered
    When a training on "2024-05-10" lasting 2.0 hours is added for trainee "john.doe1" with trainer "jane.smith1"
    Then the response status is 200
    And a workload event is published

  Scenario: Adding a training for a missing trainee returns not found
    Given no trainee is registered with username "ghost"
    And a trainer "jane.smith1" named "jane" "smith" is registered
    When a training on "2024-05-10" lasting 2.0 hours is added for trainee "ghost" with trainer "jane.smith1"
    Then the response status is 404
    And no workload event is published

  Scenario: Adding a training without a duration is rejected
    When a training with no duration is added for trainee "john.doe1" with trainer "jane.smith1"
    Then the response status is 400
    And no workload event is published
