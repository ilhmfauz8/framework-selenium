package com.example.app.stepdefs;

import com.example.app.CommonActions;
import io.cucumber.java.en.When;

public class FilterLanguageSteps {

    @When("I choose {string} article in the Home page")
    public void i_choose_article_in_the_home_page(String field) {
        CommonActions.click(field);
    }

}
