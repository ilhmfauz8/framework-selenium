package com.example.app.stepdefs;

import com.example.app.CommonActions;
import com.example.app.DriverManager;
import com.example.app.TestContext;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.example.app.Report.ExtentReportManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import java.util.logging.Logger;



public class Hooks {

	private static final Logger logger = Logger.getLogger(Hooks.class.getName());
	private static ExtentReports extent = ExtentReportManager.getInstance();
	private ExtentTest test;

	@BeforeAll
	public static void beforeAll() {
		logger.info("===== TEST EXECUTION STARTED =====");
		TestContext.getTestContext();
		CommonActions.loadRepository();
	}

	@Before
	public void before(Scenario scenario) {
		logger.info("Starting Scenario: " + scenario.getName());
		TestContext.getTestContext().setScenario(scenario);
		test = extent.createTest(scenario.getName());
	}

	@After
	public void after(Scenario scenario) {
		if (!scenario.getStatus().equals(io.cucumber.java.Status.PASSED)) {
			logger.warning("Scenario Failed: " + scenario.getName());
			byte[] screenshot = CommonActions.takeScreenshot();
			TestContext.getTestContext().getScenario().attach(screenshot, "image/png", "screenshot");
			test.log(Status.FAIL, "Scenario Failed");
			test.addScreenCaptureFromPath("screenshot.png");
		} else {
			logger.info("Scenario Passed: " + scenario.getName());
			test.log(Status.PASS, "Scenario Passed");
		}
		CommonActions.closeApplication();
		extent.flush();
		logger.info("===== TEST EXECUTION FINISHED =====");
	}

}
