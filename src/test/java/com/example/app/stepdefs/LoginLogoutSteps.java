package com.example.app.stepdefs;

import com.example.app.CommonActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginLogoutSteps {
    @When("I am click {string} button")
    public void iAmClickButton(String field) {
        CommonActions.click(field);
    }

    @Then("I click on {string} button")
    public void iClickOnButton(String field) {
        CommonActions.click(field);
    }


}
