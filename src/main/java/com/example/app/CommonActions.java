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
    private static Map<String, Map<String, String>> repository;
    private static Map<String, String> currentPagelocators;

    public static void loadRepository() {
        Yaml yaml = new Yaml();
        InputStream inputStream = CommonActions.class.getClassLoader().getResourceAsStream("repo.yaml");
        repository = yaml.load(inputStream);
    }

    public static void loadCurrentPageLocators(String page) {
        currentPagelocators = repository.get(page);
    }

    public static By getXpath(String field) {
        return By.xpath(currentPagelocators.get(field));
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
        return DriverManager.getDriver().findElement(getXpath(field));
    }

    private static WebElement findElement(By xpath) {
        return DriverManager.getDriver().findElement(xpath);
    }

    private static List<WebElement> findElements(By xpath) {
        // TODO
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfAllElementsLocatedBy(xpath));
        return DriverManager.getDriver().findElements(xpath);
    }


    public static void click(String field) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getXpath(field)));
        findElement(getXpath(field)).click();
    }


    public static void doubleClick(String field) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getXpath(field)));
        WebElement element = findElement(getXpath(field));
        Actions actions = new Actions(DriverManager.getDriver());
        actions.doubleClick(element).perform();

        // Verifikasi apakah aksi berhasil dengan menunggu perubahan state pada elemen
        waitUntilExpectedCondition(ExpectedConditions.attributeToBeNotEmpty(element, "class"));

        //TODO
    }


    public static void enterText(String field, String text) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getXpath(field)));
        findElement(getXpath(field)).sendKeys(text);
    }

    public static void clearText(String field, String text) {
        waitUntilExpectedCondition(ExpectedConditions.elementToBeClickable(getXpath(field)));
        findElement(getXpath(field)).clear();
    }

    public static String getText(String field) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getXpath(field)));
        String actualText = findElement(getXpath(field)).getText();

        // Cetak teks untuk debugging
//        System.out.println("XPath: " + getXpath(field) + " | Actual text found: " + actualText);
        return actualText;
    }

    private static <V> V waitUntilExpectedCondition(ExpectedCondition<V> expectedCondition) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15));
        return wait.until(expectedCondition);
    }

    private static <V> V waitUntilExpectedCondition(ExpectedCondition<V> expectedCondition, int timeoutInsSecs) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInsSecs));
        return wait.until(expectedCondition);
    }

    public static void selectTextFromDropDown(String field, String text) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getXpath(field)));
        Select select = new Select(findElement(getXpath(field)));
        select.selectByVisibleText(text);
    }

    public static void selectValueFromDropDown(String field, String value) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getXpath(field)));
        Select select = new Select(findElement(getXpath(field)));
        select.selectByValue(value);
    }

    public static void selectIndexFromDropDown(String field, int index) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getXpath(field)));
        Select select = new Select(findElement(getXpath(field)));
        select.selectByIndex(index);
    }

    public static void deselectAllFromDropDown(String field) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getXpath(field)));
        Select select = new Select(findElement(getXpath(field)));
        select.deselectAll();
    }

    public static List<WebElement> getAllSelectedOptionsFromDropDown(String field) {
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getXpath(field)));
        Select select = new Select(findElement(getXpath(field)));
        return select.getAllSelectedOptions();
    }

    public static List<WebElement> getAllOptionsFromDropDown(String field) {
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getXpath(field)));
        Select select = new Select(findElement(getXpath(field)));
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
        waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getXpath(field)));
        try {
            return findElement(field).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementEnabled(String field) {
        try {
            waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getXpath(field)));
            return findElement(field).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementDisabled(String field) {
        try {
            waitUntilExpectedCondition(ExpectedConditions.visibilityOfElementLocated(getXpath(field)));
            return !findElement(field).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTitle() {
        return DriverManager.getDriver().getTitle();
    }

    public static String getElementAttribute(String field, String attribute) {
        waitUntilExpectedCondition(ExpectedConditions.presenceOfElementLocated(getXpath(field)));
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

    // Draws a red border around the found element. Does not set it back anyhow.
    public WebElement highLightElement(String field) {
        WebElement elem = findElement(field);
        // draw a border around the found element
        if (DriverManager.getDriver() instanceof JavascriptExecutor) {
            ((JavascriptExecutor)DriverManager.getDriver()).executeScript("arguments[0].style.border='3px solid red'", elem);
        }
        return elem;
    }

    public void scrollDown(){
        executeJavaScript("window.scrollBy(0, 1000);");
    }

    public void scrollToElement(String field){
        WebElement element =findElement(field);
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }
    public Object executeJavaScript(String javaScript){
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        return js.executeScript(javaScript);
    }

    public Object executeJavaScript(String javaScript, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        return js.executeScript(javaScript, element);
    }
    //TODO
    //doAction
public static void doAction(String locator, CommonActions.Events event){
    Actions actions = new Actions(DriverManager.getDriver());
    WebElement element = findElement(locator);
    switch (event) {
        case CLICK:
            click(locator);
            System.out.println("Clicked on: " + locator);
            break;
        case DOUBLE_CLICK:
            actions.doubleClick(element).perform();
            System.out.println("Double-clicked on: " + locator);
            break;
        case RIGHT_CLICK:
            actions.contextClick(element).perform();
            System.out.println("Right-clicked on: " + locator);
            break;
        case CTRL_CLICK:
            actions.keyDown(Keys.LEFT_CONTROL).click(element).keyUp(Keys.LEFT_CONTROL).perform();
            System.out.println("Ctrl+Clicked on: " + locator);
            break;
        case SHIFT_CLICK:
            actions.keyDown(Keys.LEFT_SHIFT).click(element).keyUp(Keys.LEFT_SHIFT).perform();
            System.out.println("Shift+Clicked on: " + locator);
            break;
        case MOUSE_HOVER:
            actions.moveToElement(element).perform();
            System.out.println("Hovered on: " + locator);
            break;
        case ENTER_KEY:
            actions.sendKeys(element, Keys.ENTER).perform();
            System.out.println("Pressed Enter on: " + locator);
            break;
        case ESC_KEY:
            actions.sendKeys(element, Keys.ESCAPE).perform();
            System.out.println("Pressed Escape on: " + locator);
            break;

    }
}
    public static enum Events{
        CLICK,
        DOUBLE_CLICK,
        CTRL_CLICK,
        RIGHT_CLICK,
        SHIFT_CLICK,
        MOUSE_UP,
        MOUSE_DOWN,
        MOUSE_HOVER,
        ENTER_KEY,
        ESC_KEY;

        private Events(){}
    }
}
