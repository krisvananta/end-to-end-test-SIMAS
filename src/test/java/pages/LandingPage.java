package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By masukButton = By.linkText("Masuk");

    public LandingPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void clickMasuk() {
        WebElement btn = driver.findElement(masukButton);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }
}
