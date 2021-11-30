package project;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.After;

import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.*;

public class Stepdefs {

    WebDriver driver = new HtmlUnitDriver();
    String url = "http://localhost:5000";


    @Given("login is selected")
    public void loginSelected() {
        clickLink("Login");
        assertTrue(driver.getPageSource().contains("Username:"));
        assertTrue(driver.getPageSource().contains("Password:"));
        assertTrue(driver.getPageSource().contains("Login"));
    }

    @When("given correct credentials username {string} and password {string}")
    public void correctUsernameAndPassword(String username, String password) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
        findElementAndSubmit("login");
    }

    @When("given correct username {string} and incorrect password {string}")
    public void correctUsernameAndIncorrectPassword(String username, String password) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
        findElementAndSubmit("login");
    }

    @When("given non existing username {string}")
    public void nonExistingUsername(String username) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", "something");
        findElementAndSubmit("login");
    }

    @Then("user login is successful")
    public void loginSuccessful() {
        assertTrue(driver.getPageSource().contains("Welcome,"));
    }

    @Then("login fails")
    public void loginFails() {
        assertTrue(driver.getPageSource().contains("Wrong username or password"));
    }

    @Given("list is selected")
    public void listSelected() {
        clickLink("List");
    }

    @Then("recommendations page is shown")
    public void recommendationsListed() {
        assertTrue(driver.getPageSource().contains("Headline"));
        assertTrue(driver.getPageSource().contains("Type"));
    }

    @Given("signup is selected")
    public void signupSelected() {
        clickLink("Signup");
        assertTrue(driver.getPageSource().contains("Username:"));
        assertTrue(driver.getPageSource().contains("Password:"));
        assertTrue(driver.getPageSource().contains("Register"));
    }

    @When("given valid username {string} and valid password {string}")
    public void validUsernameAndPassword(String username, String password) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
    }

    @When("given valid username {string} and invalid password {string}")
    public void validUsernameAndInvalidPassword(String username, String password) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
    }

    @When("given invalid username {string}")
    public void invalidUsername(String username) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", "something");
    }

    @Then("signup fails")
    public void signupFails() {
        assertTrue(driver.getPageSource().contains("Failure"));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void findElementAndSendData(String name, String value) {
        WebElement elem = driver.findElement(By.name(name));
        elem.sendKeys(value);
    }

    private void findElementAndSubmit(String name) {
        WebElement elem = driver.findElement(By.name(name));
        elem.submit();
    }

    private void clickLink(String link) {
        driver.get(url);
        WebElement elem = driver.findElement(By.linkText(link));
        elem.click();
    }



}