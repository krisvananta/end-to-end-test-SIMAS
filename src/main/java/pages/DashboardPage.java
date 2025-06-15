package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {

    WebDriver driver;

    @FindBy(id = "adminGreeting")
    WebElement greeting;

    @FindBy(id = "adminName")
    WebElement adminName;

    @FindBy(id = "wargaListContainer")
    WebElement wargaListContainer;

    @FindBy(id = "kritikContainer")
    WebElement kritikContainer;

    @FindBy(id = "pembayaranList")
    WebElement pembayaranList;

    @FindBy(id = "pemasukanAmount")
    WebElement pemasukanAmount;

    @FindBy(id = "pengeluaranAmount")
    WebElement pengeluaranAmount;

    @FindBy(id = "pieChart")
    WebElement pieChart;

    @FindBy(id = "barChart")
    WebElement barChart;

    @FindBy(id = "lineChart")
    WebElement lineChart;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isGreetingDisplayed() {
        return greeting.isDisplayed();
    }

    public String getAdminName() {
        return adminName.getText();
    }

    public boolean isWargaLoaded() {
        return !wargaListContainer.getText().contains("Loading...");
    }

    public boolean isKritikLoaded() {
        return !kritikContainer.getText().contains("Loading...");
    }

    public boolean isPembayaranLoaded() {
        return !pembayaranList.getText().contains("Loading...");
    }

    public boolean isFinancialAmountVisible() {
        return !(pemasukanAmount.getText().contains("Loading") || pengeluaranAmount.getText().contains("Loading"));
    }

    public boolean isPieChartVisible() {
        return pieChart.isDisplayed();
    }

    public boolean isBarChartVisible() {
        return barChart.isDisplayed();
    }

    public boolean isLineChartVisible() {
        return lineChart.isDisplayed();
    }
}
