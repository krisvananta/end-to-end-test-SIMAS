package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    WebDriver driver;

    @FindBy(id = "floatingEmail")
    WebElement emailField;

    @FindBy(id = "floatingPassword")
    WebElement passwordField;

    @FindBy(className = "submit-btn")
    WebElement loginButton;

    @FindBy(id = "loginError")
    WebElement loginError;

    @FindBy(id = "loginSuccess")
    WebElement loginSuccess;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public String getSuccessMessage() {
        try {
            return loginSuccess.isDisplayed() ? loginSuccess.getText() : "";
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            return loginError.isDisplayed() ? loginError.getText() : "";
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public boolean isOnLoginPage() {
        return driver.getTitle().contains("Login");
    }

    public void loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }
}
