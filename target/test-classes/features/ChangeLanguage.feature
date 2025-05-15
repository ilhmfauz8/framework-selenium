@ChangeLanguage
Feature: Wikipedia Change Language Functionality

  Background:
    Given I open browser
    And navigate to application

  @ChangeLanguageINA
  Scenario: Search for a existing term and change the language
    And I am on "Home" page
    And the title is "Wikipedia"
    When I enter "Selenium" in the "searchTextBox"
    And I select "English" from dropdown "searchLanguage"
    And I click on "searchButton"
    Then I am on "Content" page
    And text of "header" is "Selenium"
    When I choose "filterLanguage" in result of Selenium
    And I click on "BahasaIndonesia"
    And text of "textBahasa" is "Dari Wikipedia bahasa Indonesia, ensiklopedia bebas"
