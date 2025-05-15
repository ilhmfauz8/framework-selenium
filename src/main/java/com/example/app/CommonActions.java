package com.example.app;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.yaml.snakeyaml.Yaml;

public class CommonActions {
    private static Map<String, Map<String, Map<String, String>>> repository;
    private static Map<String, Map<String, String>> currentPagelocators;

    public static void setPage(String page) {
        currentPagelocators = repository.get(page);
    }

    public static void loadRepository() {
        Yaml yaml = new Yaml();
        InputStream inputStream = CommonActions.class.getClassLoader().getResourceAsStream("repo.yaml");
        repository = yaml.load(inputStream);
    }

    public static void loadCurrentPageLocators(String page) {
        currentPagelocators = repository.get(page);
    }

    public static By getLocator(String field) {
        Map<String, String> fieldInfo = currentPagelocators.get(field);
        String type = fieldInfo.get("type");
        String value = fieldInfo.get("value");

        return switch (type.toLowerCase()) {
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "class" -> By.className(value);
            case "tag" -> By.tagName(value);
            case "css" -> By.cssSelector(value);
            case "linktext" -> By.linkText(value);
            case "partiallinktext" -> By.partialLinkText(value);
            case "xpath" -> By.xpath(value);
            default -> throw new IllegalArgumentException("Unknown locator type: " + type);
        };
    }

    public static void navigateTo(String url) {
        DriverManager.getDriver().get(url);
        maximizeApplication();
    }

    public static void closeApplication() {
        DriverManager.getDriver().close();
        DriverManager.getDriver().quit();
        DriverManager.setDriver(null);
    }

    public static void maximizeApplication() {
        DriverManager.getDriver().manage().window().maximize();
    }

    public static byte[] takeScreenshot() {
        TakesScreenshot takesScreenshot = (TakesScreenshot) DriverManager.getDriver();
        return takesScreenshot.getScreenshotAs(OutputType.BYTES);
    }

    private static WebElement findElement(String field) {
        return DriverManager.getDriver().findElement(getLocator(field));
    }

    private static WebElement findElement(By locator) {
        return DriverManager.getDriver().findElement(locator);
    }

    private static List<WebElement> findElements(By locator) {
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        return DriverManager.getDriver().findElements(locator);
    }

    public static void click(String field) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getLocator(field)));
        findElement(field).click();
    }

    public static void doubleClick(String field) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getLocator(field)));
        WebElement element = findElement(field);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.doubleClick(element).perform();

        waitUntilExpectedCondition(ExpectedConditions.attributeToBeNotEmpty(element, "class"));
    }

    public static void enterText(String field, String text) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getLocator(field)));
        findElement(field).sendKeys(text);
    }

    public static void clearText(String field) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getLocator(field)));
        findElement(field).clear();
    }

    public static String getText(String field) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getLocator(field)));
        String actualText = findElement(field).getText();
        return actualText;
    }

    private static <V> V waitUntilExpectedCondition(ExpectedCondition<V> expectedCondition) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15));
        return wait.until(expectedCondition);
    }

    private static <V> V waitUntilExpectedCondition(ExpectedCondition<V> expectedCondition, int timeoutInSecs) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSecs));
        return wait.until(expectedCondition);
    }

    public static void selectTextFromDropDown(String field, String text) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getLocator(field)));
        Select select = new Select(findElement(field));
        select.selectByVisibleText(text);
    }

    public static void selectValueFromDropDown(String field, String value) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getLocator(field)));
        Select select = new Select(findElement(field));
        select.selectByValue(value);
    }

    public static void selectIndexFromDropDown(String field, int index) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getLocator(field)));
        Select select = new Select(findElement(field));
        select.selectByIndex(index);
    }

    public static void deselectAllFromDropDown(String field) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getLocator(field)));
        Select select = new Select(findElement(field));
        select.deselectAll();
    }

    public static List<WebElement> getAllSelectedOptionsFromDropDown(String field) {
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getLocator(field)));
        Select select = new Select(findElement(field));
        return select.getAllSelectedOptions();
    }

    public static List<WebElement> getAllOptionsFromDropDown(String field) {
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getLocator(field)));
        Select select = new Select(findElement(field));
        return select.getOptions();
    }

    public static List<String> getTextOfAllElements(List<WebElement> elements) {
        List<String> elementsText = new ArrayList<>();
        for (WebElement element : elements) {
            elementsText.add(element.getText());
        }
        return elementsText;
    }

    public static boolean isElementDisplayed(String field) {
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getLocator(field)));
        try {
            return findElement(field).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementEnabled(String field) {
        try {
            waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getLocator(field)));
            return findElement(field).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementDisabled(String field) {
        try {
            waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getLocator(field)));
            return !findElement(field).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTitle() {
        return DriverManager.getDriver().getTitle();
    }

    public static String getElementAttribute(String field, String attribute) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getLocator(field)));
        return findElement(field).getAttribute(attribute);
    }

    public static void switchToWindowByTitle(final String windowTitle) {
        if (!getTitle().equals(windowTitle)) {
            Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
            for (String windowHandle : windowHandles) {
                DriverManager.getDriver().switchTo().window(windowHandle);
                if (DriverManager.getDriver().getTitle().equals(windowTitle)) {
                    break;
                }
            }
            if (!DriverManager.getDriver().getTitle().equals(windowTitle)) {
                throw new NoSuchWindowException(String.format("No such window found with title: %s", windowTitle));
            }
        }
    }

    public static void refreshPage() {
        DriverManager.getDriver().navigate().refresh();
    }

    public static String acceptAlert(){
        waitUntilExpectedCondition(ExpectedConditions.alertIsPresent());
        Alert alert=DriverManager.getDriver().switchTo().alert();
        String text= alert.getText();
        alert.accept();
        return text;
    }

    public static String dismissAlert(){
        waitUntilExpectedCondition(ExpectedConditions.alertIsPresent());
        Alert alert=DriverManager.getDriver().switchTo().alert();
        String text= alert.getText();
        alert.dismiss();
        return text;
    }

    public static void switchToFrame(String nameOrId){
        waitUntilExpectedCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
        DriverManager.getDriver().switchTo().frame(nameOrId);
    }

    public static void switchToFrame(int index){
        waitUntilExpectedCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
        DriverManager.getDriver().switchTo().frame(index);
    }

    public static void switchToFrame(By locator){
        waitUntilExpectedCondition(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
        DriverManager.getDriver().switchTo().frame(findElement(locator));
    }

    public static void switchToParentFrame(){
        DriverManager.getDriver().switchTo().parentFrame();
    }

    public static String getRecentDownloadedFile() {
        String sdir=TestContext.getTestContext().getApplicationProperty("download.dir");
        File dir = new File(sdir);
        if (dir.isDirectory()) {
            Optional<File> opFile = Arrays.stream(dir.listFiles(File::isFile))
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

            if (opFile.isPresent()){
                return opFile.get().getName();
            }
        }
        return null;
    }

    public WebElement highLightElement(String field) {
        WebElement elem = findElement(field);
        if (DriverManager.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor)DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid red'", elem);
        }
        return elem;
    }

    public void scrollDown(){
        executeJavaScript("window.scrollBy(0, 1000);");
    }

    public void scrollToElement(String field){
        WebElement element = findElement(field);
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    public void executeJavaScript(String javaScript){
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript(javaScript);
    }

    public void executeJavaScript(String javaScript, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript(javaScript, element);
    }

//    public static void doAction(String locator, CommonActions.Events event){
//        Actions actions = new Actions(DriverManager.getDriver());
//        WebElement element = findElement(locator);
//        switch (event) {
//            case CLICK:
//                click(locator);
//                System.out.println("Clicked on: " + locator);
//                break;
//            case DOUBLE_CLICK:
//                actions.doubleClick(element).perform();
//                System.out.println("Double-clicked on: " + locator);
//                break;
//            case RIGHT_CLICK:
//                actions.contextClick(element).perform();
//                System.out.println("Right-clicked on: " + locator);
//                break;
//            case CTRL_CLICK:
//                actions.keyDown(Keys.LEFT_CONTROL).click(element).keyUp(Keys.LEFT_CONTROL).perform();
//                System.out.println("Ctrl+Clicked on: " + locator);
//                break;
//            case SHIFT_CLICK:
//                actions.keyDown(Keys.LEFT_SHIFT).click(element).keyUp(Keys.LEFT_SHIFT).perform();
//                System.out.println("Shift
//        }
//    }
}