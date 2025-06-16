package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailInput = By.id("floatingEmail");
    private By passwordInput = By.id("floatingPassword");
    private By loginButton = By.xpath("//button[text()='Masuk']");

    // Diperbarui: Terima WebDriverWait dari luar
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void setEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
    }

    public void setPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void loginAsAdmin(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
        this.clickLoginButton();
    }
}