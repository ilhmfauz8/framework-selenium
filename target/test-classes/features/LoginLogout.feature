@LoginLogout
Feature: Wikipedia Login Logout Functionality

  Background:
    Given I open browser
    And navigate to application

  @Login
  Scenario: Login account wikipedia
    And I am on "Home" page
    And the title is "Wikipedia"
    When I choose "ENGVersion" article in the Home page
    Then I am on "EnglishVersion" page
    And text of "header" is "Welcome to Wikipedia"
    When I am click "LoginNavbar" button
    Then I am on "Login" page
    And I enter "ilhmfauz" in the "username"
    And I enter "ilham123" in the "password"
    And I click on "login" button
    Then "userPage" is displayed
    And "userNotification" is displayed
    And I take screenshot

  @Logout
  Scenario: Logout account wikipedia
    And I am on "Home" page
    And the title is "Wikipedia"
    When I choose "ENGVersion" article in the Home page
    Then I am on "EnglishVersion" page
    And text of "header" is "Welcome to Wikipedia"
    When I am click "LoginNavbar" button
    Then I am on "Login" page
    And I enter "ilhmfauz" in the "username"
    And I enter "ilham123" in the "password"
    And I click on "login" button
    Then "userPage" is displayed
    And "userNotification" is displayed
    And "profileDropdown" is displayed
    When I am click "profileDropdown" button
    And I click on "logout" button
    Then text of "logoutResults" contains "Log out"
    And I take screenshot




