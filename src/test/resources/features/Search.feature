@Search @RegressionTest
Feature: Wikipedia Search Functionality

  Background:
    Given I open browser
    And navigate to application

  @tag1
  Scenario: Search for a existing term
    And I am on "Home" page
    And the title is "Wikipedia"
    When I enter "Selenium" in the "searchTextBox"
    And I select "English" from dropdown "searchLanguage"
    And I click on "searchButton"
    Then I am on "Content" page
    And text of "header" is "Selenium"
    And I take screenshot

  @tag2
  Scenario: Search for a non-existing term
    And I am on "Home" page
    And the title is "Wikipedia"
    When I enter "abcxyz123" in the "searchTextBox"
    And I select "Bahasa Indonesia" from dropdown "searchLanguage"
    And I click on "searchButton"
    Then I am on "SearchResults" page
    And text of "searchResults" contains "Tidak ada hasil yang sesuai dengan kriteria."
    And I take screenshot

  @tag3
  Scenario: Search for a generic term
    And I am on "Home" page
    And the title is "Wikipedia"
    When I enter "uncommon word" in the "searchTextBox"
    And I select "Bahasa Indonesia" from dropdown "searchLanguage"
    And I click on "searchButton"
    Then I am on "SearchResults" page
    And text of "totalResults" is "3"
    And I take screenshot
