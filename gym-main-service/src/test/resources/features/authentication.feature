Feature: Authentication
  As a registered member
  I want to log in with my credentials
  So that I receive a token for protected operations

  Scenario: Logging in with valid credentials returns a token
    Given the credentials for "john.doe1" are valid
    When "john.doe1" logs in with password "Secret123"
    Then the response status is 200
    And the response carries a token

  Scenario: Logging in with invalid credentials is rejected
    Given the credentials for "john.doe1" are invalid
    When "john.doe1" logs in with password "wrong-pass"
    Then the response status is 401
