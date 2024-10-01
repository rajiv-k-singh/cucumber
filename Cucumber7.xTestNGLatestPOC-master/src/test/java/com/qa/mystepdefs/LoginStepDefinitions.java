package com.qa.mystepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import pages.LoginPage;

public class LoginStepDefinitions {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>(); // Use ThreadLocal for parallel execution
    private LoginPage loginPage;

    @Before
    public void setup() {
        if (driver.get() == null) { // Only initialize if the driver is null
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-extensions");
            driver.set(new ChromeDriver(options)); // Set the WebDriver instance in ThreadLocal
        }
    }

    @After
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit(); // Quit the WebDriver for this thread
            driver.remove(); // Remove the WebDriver reference
        }
    }

    @Given("I am on the OpenCart login page")
    public void i_am_on_the_open_cart_login_page() {
        driver.get().get("https://naveenautomationlabs.com/opencart/index.php?route=account/login");
        loginPage = new LoginPage(driver.get()); // Pass the WebDriver instance
    }

    @Given("I have entered a valid username and password")
    public void i_have_entered_a_valid_username_and_password() {
        loginPage.enterEmail("qatestertest@gmail.com");
        loginPage.enterPassword("Test@123");
    }

    @Given("I have entered invalid {string} and {string}")
    public void i_have_entered_invalid_and(String username, String password) {
        loginPage.enterEmail(username);
        loginPage.enterPassword(password);
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        Assert.assertEquals(loginPage.checkLogoutLink(), true);
    }

    @Then("I should see an error message indicating {string}")
    public void i_should_see_an_error_message_indicating(String errorMessage) {
        Assert.assertEquals(driver.get().findElement(By.cssSelector(".alert-danger")).isDisplayed(), true);
    }

    @When("I click on the \"Forgotten Password\" link")
    public void i_click_on_the_forgotten_password_link() {
        loginPage.clickForgottenPasswordLink();
    }

    @Then("I should be redirected to the password reset page")
    public void i_should_be_redirected_to_the_password_reset_page() {
        Assert.assertTrue(loginPage.getForgotPwdPageUrl().contains("account/forgotten"));
    }

    public static WebDriver getDriver() {
        return driver.get(); // Static method to retrieve the WebDriver instance
    }
}
