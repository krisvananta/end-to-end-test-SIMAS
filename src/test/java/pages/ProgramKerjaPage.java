package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProgramKerjaPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By addNewButton = By.xpath("//button[normalize-space()='+ ADD NEW']");
    private By upcomingTab = By.cssSelector("button[data-filter='upcoming']");

    // Diperbarui: Terima WebDriverWait dari luar
    public ProgramKerjaPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void clickAddNew() {
        System.out.println("Mencari tombol '+ ADD NEW'...");
        wait.until(ExpectedConditions.elementToBeClickable(addNewButton)).click();
        System.out.println("Berhasil mengklik 'Add New'.");
    }

    public void clickUpcomingTab() {
        System.out.println("Mencari tombol tab 'Upcoming'...");
        wait.until(ExpectedConditions.elementToBeClickable(upcomingTab)).click();
        System.out.println("Berhasil mengklik tab 'Upcoming'.");
    }
}