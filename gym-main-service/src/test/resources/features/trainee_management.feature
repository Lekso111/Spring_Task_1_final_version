Feature: Trainee management
  As the gym service
  I want to register and look up trainees
  So that members can be managed through the API

  Scenario: Registering a new trainee returns generated credentials
    Given the username "john.doe1" is not taken yet
    When a trainee is registered with first name "john" and last name "doe"
    Then the response status is 200
    And the returned username is "john.doe1"
    And the response carries a generated password

  Scenario: Registering a trainee without a first name is rejected
    When a trainee is registered with a blank first name
    Then the response status is 400

  Scenario: Fetching an existing trainee profile
    Given a trainee "john.doe1" named "john" "doe" is registered
    When the profile of trainee "john.doe1" is requested
    Then the response status is 200
    And the returned first name is "john"

  Scenario: Fetching a missing trainee profile returns not found
    Given no trainee is registered with username "ghost"
    When the profile of trainee "ghost" is requested
    Then the response status is 404
