package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminDashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By programKerjaMenu = By.xpath("//a[normalize-space()='Program Kerja']");

    // Diperbarui: Terima WebDriverWait dari luar
    public AdminDashboardPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void goToProgramKerja() {
        System.out.println("Mencari link dengan XPath: //a[normalize-space()='Program Kerja']");
        wait.until(ExpectedConditions.elementToBeClickable(programKerjaMenu)).click();
        System.out.println("Berhasil mengklik 'Program Kerja'.");
    }
}