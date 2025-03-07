@FilterLanguage
Feature: Wikipedia Filter Language Homepage Functionality

  Background:
    Given I open browser
    And navigate to application

  @tag1
  Scenario: Search for a existing term
    And I am on "Home" page
    And the title is "Wikipedia"
    When I choose "ENGVersion" article in the Home page
    Then I am on "EnglishVersion" page
    And text of "header" is "Welcome to Wikipedia"
    And I take screenshot


