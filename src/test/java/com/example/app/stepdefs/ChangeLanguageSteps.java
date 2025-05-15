package com.example.app.stepdefs;

import com.example.app.CommonActions;
import io.cucumber.java.en.When;

public class ChangeLanguageSteps {
    @When("I choose {string} in result of Selenium")
    public void iChooseInResultOfSelenium(String field) {
        CommonActions.click(field);
    }
}
