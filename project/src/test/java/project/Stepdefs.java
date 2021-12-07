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


    //Login steps
    @Given("login is selected")
    public void loginSelected() {
        clickLink("Login");
        assertTrue(driver.getPageSource().contains("Username:"));
        assertTrue(driver.getPageSource().contains("Password:"));
        assertTrue(driver.getPageSource().contains("Login"));
    }

    @Given("user with username {string} and password {string} is created")
    public void userCreated(String username, String password) {
        clickLink("Signup");
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
        findElementAndSubmit("register");
    }

    @Given("user is logged in")
    public void userLoggedIn() {
        clickLink("Login");
        findElementAndSendData("username", "tester");
        findElementAndSendData("password", "salasana123");
        findElementAndSubmit("login");
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

    //Listing steps
    @Given("list is selected")
    public void listSelected() {
        clickLink("List");
    }

    @Then("recommendations page is shown")
    public void recommendationsListed() {
        assertTrue(driver.getPageSource().contains("Headline"));
        assertTrue(driver.getPageSource().contains("Type"));
    }

    @Then("all recommendations are shown")
    public void allRecommendationsAreShown() {
        WebElement elem = driver.findElement(By.name("recommendations"));

        assertTrue(elem.getText().contains("Blog 1"));
        assertTrue(elem.getText().contains("Book 1"));
    }

    //Signup steps
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
        findElementAndSubmit("register");
    }

    @When("given valid username {string} and invalid password {string}")
    public void validUsernameAndInvalidPassword(String username, String password) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
        findElementAndSubmit("register");
    }

    @When("given invalid username {string} and valid password {string}")
    public void invalidUsernameAndValidPasswors(String username, String password) {
        findElementAndSendData("username", username);
        findElementAndSendData("password", password);
        findElementAndSubmit("register");
    }

    @Then("signup is successful")
    public void signupIsSuccessful() {
        assertTrue(driver.getPageSource().contains("Success"));
    }

    @Then("signup fails")
    public void signupFails() {
        assertTrue(driver.getPageSource().contains("Failure"));
    }

    //Recommendation adding steps
    @Given("post is selected")
    public void newIsSelected() {
        clickLink("Post");
    }

    @Given("blogpost is selected")
    public void blogPostIsSelected() {
        clickPostType("blogi");
    }

    @Given("bookpost is selected")
    public void bookPostIsSelected() {
        clickPostType("kirja");
    }
 
    @When("given valid blog headline {string}, writer {string} and url {string}")
    public void validBlogData(String headline, String writer, String url) {
        findElementAndSendData("url", url);
        findElementAndSendData("headline", headline);
        findElementAndSendData("writer", writer);
        findElementAndSubmit("add");
    }

    @When("given invalid blog, missing the headline")
    public void invalidBlogMissingHeadline() {
        findElementAndSendData("url", "Some url");
        findElementAndSendData("writer", "tester");
        findElementAndSubmit("add");
        System.out.println(driver.getPageSource());
    }

    @When("given valid book with headline {string}, writer {string} and ISBN {string}")
    public void validBookData(String headline, String writer, String isbn) {
        findElementAndSendData("headline", headline);
        findElementAndSendData("writer", writer);
        findElementAndSendData("ISBN", isbn);
        findElementAndSubmit("add");
    }

    @Then("new blog recommendation is added")
    public void blogIsAdded() {
        assertTrue(driver.getPageSource().contains("Success"));
    }

    @Then("new book recommendation is added")
    public void bookIsAdded() {
        assertTrue(driver.getPageSource().contains("Success"));
    }

    @Then("adding reading recommendation fails")
    public void recommendationAddingFails() {
        assertTrue(driver.getPageSource().contains("Failure"));
    }

    @Then("user is returned to the mainpage")
    public void returnedToMain() {
        assertTrue(driver.getPageSource().contains("Front page"));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void pageContains(String content) {
        assertTrue(driver.getPageSource().contains(content));
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

    private void clickPostType(String type) {
        WebElement elem = driver.findElement(By.name(type));
        elem.click();
    }



}